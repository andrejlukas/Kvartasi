import { Navbar } from "../../components/Navbar";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "leaflet/dist/leaflet.css";
import "../../styles/MojeRecenzije.css";

export function MojeRecenzije() {
    const navigate = useNavigate();
   const [id, setId] = useState("")
   const [emailAddress, setEmailAddress] = useState('')
    const [trgovina, setTrgovina] = useState({});
   


   const [recenzije, SetRecenzije] = useState([])
   const [error, setError] = useState(null);

   /* ZA PROVJERU
   useEffect(() => {
   console.log("ID ažuriran:", id);
}, [id]); */

   //dohvacanje mail-a
   useEffect(() => {
      const token = localStorage.getItem('token');
      var options = {
          method: 'POST',
          headers: {
              'Authorization': `Bearer ${token}`,
              'Content-Type': 'application/json'
          },
          body: JSON.stringify({ oneLiner: token })
      }

      fetch('/api/tokens/claims', options)
          .then(response => {
              if (!response.ok) {
                  throw new Error('Network response was not ok');
              }
              return response.json();
          })
          .then(data => {
            setEmailAddress(data.email);
          })
          .catch(error => console.error('There was a problem with the fetch operation: ', error));
  }, []); //prazno, radi se na pocetku samo

  useEffect(() => {
   const token = localStorage.getItem('token');
   const options = {
       method: 'GET',
       headers: {
           'Authorization': `Bearer ${token}`,
           'Content-Type': 'application/json'
       }
   };

   // Dohvaćanje korisničkih podataka ako postoji emaila
   if (emailAddress) {
       fetch(`/api/kupacs/${emailAddress}`, options)
           .then(response => {
               if (!response.ok) {
                   throw new Error('Network response was not ok');
               }
               return response.json();
           })
           .then(data => {
               setId(data.kupacId);
           })
           .catch(error => {
               console.error('There was a problem with the fetch operation: ', error);
               setError(error.message);
           });
   }
}, [emailAddress]);

useEffect(() => {
   const token = localStorage.getItem('token');
   const options = {
       method: 'GET',
       headers: {
           'Authorization': `Bearer ${token}`,
           'Content-Type': 'application/json'
       }
   };

   // Dohvaćanje recenzija preko id-a
   console.log(id)
   if (id) {
       fetch(`/api/recenzijas/kupacs/${id}`, options)
           .then(response => {
               if (!response.ok) {
                   throw new Error('Network response was not ok');
               }
               return response.json();
           })
           .then(data => {
               console.log(data)
               SetRecenzije(data)
               data.forEach((element) =>
                fetchTrgovinaName(element.trgovinaId)
                
            )
           })
           .catch(error => {
               console.error('There was a problem with the fetch operation: ', error);
               setError(error.message);
           });
   }
   const fetchTrgovinaName = async (trgovinaId) => {
    // console.log(trgovinaId)
    try {
       const response = await fetch(`/api/trgovinas/getById/${trgovinaId}`, options);
       if (!response.ok) {
          throw new Error(`Failed to fetch store with id ${trgovinaId}`);
       }
       const name = await response.json();
       // console.log(name.trgovinaNaziv)
       setTrgovina(prevNames => ({ ...prevNames, [trgovinaId]: name.trgovinaNaziv }));
    } catch (error) {
       console.error(`Error fetching store name for id ${trgovinaId}:`, error);
    }
 };
}, [id]);

/* odobrioModerator
recenzijaId   
recenzijaOdgovor
recenzijaOpis
recenzijaZvjezdice
trgovinaId
vrijemeKreiranja */
  useEffect(()=>{ console.log(trgovina)}, [trgovina]
   )

//brisanje recenzije
async function brisanjeRecenzije(poslanId) {
    console.log(`Brisanje recenzije s ID-jem: ${poslanId}`);
    const  fetchTrgovinaName = async (trgovinaId) => {
        const token = localStorage.getItem("token");
        if (!token) {
            setError("Nedostaje token za autorizaciju.");
            return;
        }
    
        const options = {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },
        };
    
        try {
            const response = await fetch(`/api/trgovinas/getById/${trgovinaId}`, options);
            if (!response.ok) {
                throw new Error(`Dohvaćanje trgovine nije uspjelo. ID: ${trgovinaId}`);
            }
    
            const trgovinaData = await response.json();
            console.log(`Dohvaćena trgovina: ${trgovinaData.trgovinaNaziv}`);
            setTrgovina((prevNames) => ({
                ...prevNames,
                [trgovinaId]: trgovinaData.trgovinaNaziv,
            }));
        } catch (error) {
            console.error(`Pogreška prilikom dohvaćanja trgovine (ID: ${trgovinaId}):`, error);
        }
    }

    const token = localStorage.getItem("token");
    if (!token) {
        setError("Nedostaje token za autorizaciju.");
        return;
    }

    const options = {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
        },
    };

    try {
        // Brisanje recenzije
        const deleteResponse = await fetch(`/api/recenzijas/${poslanId}`, options);
        if (!deleteResponse.ok) {
            throw new Error(`Brisanje recenzije nije uspjelo. Status: ${deleteResponse.status}`);
        }
        console.log(`Recenzija s ID-jem ${poslanId} uspješno obrisana.`);

        // Dohvaćanje ažuriranih recenzija
        const recenzijeOptions = {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },
        };

        const recenzijeResponse = await fetch(`/api/recenzijas/kupacs/${id}`, recenzijeOptions);
        if (!recenzijeResponse.ok) {
            throw new Error(`Dohvaćanje recenzija nije uspjelo. Status: ${recenzijeResponse.status}`);
        }

        const recenzijeData = await recenzijeResponse.json();
        console.log("Ažurirane recenzije:", recenzijeData);

        // Ažuriranje state-a
        SetRecenzije(recenzijeData);

        // Dohvaćanje imena trgovina za svaku recenziju
        await Promise.all(
            recenzijeData.map((recenzija) => fetchTrgovinaName(recenzija.trgovinaId))
        );
    } catch (error) {
        console.error("Pogreška prilikom obrade recenzija:", error);
        setError(error.message);
    }
    
}






   return (
      <div className="moje-recenzije-wrapper">
         <Navbar/>
         <div className="main-container">
            {/* <h1>Moje recenzije:</h1> */}

            <div className="moje-recenzije-container">
            {
               recenzije.length > 0 ? (
                  recenzije.map((recenzija, index) => 
                  (
                     <div key = {recenzija.recenzijaId}className="recenzija-list">
                        <div className="recenzijanaslov">Recenzija za trgovinu {
                            trgovina[recenzija.trgovinaId]
                            }</div>
                        <div className="recenzija-info-plus-button">
                            
                            {/* <div className="stupac">
                            <p>{recenzija.recenzijaId}</p>
                            </div> */}
                            
                            <div className="stupac-recenzija" id="recenzija-opisIOdgovor">
                                <div >
                                    <p>{recenzija.recenzijaOpis}</p>

                                </div>
                               
                                {
                                (recenzija.recenzijaOdgovor && recenzija.recenzijaOdgovor.length > 0 ) ?
                                (<div>
                                    <p id="recenzija-odgovortrgovine">Odgovor:</p>
                                    <p>{recenzija.recenzijaOdgovor}</p>
                                    </div>) : 
                                (<div id="recenzija-nema-odgovora"><p>Nema odgovora</p></div>)
                            }
                                
                            
                            </div>
                            <div className="stupac-recenzija" id="recenzija-dodatne-info" >
                                <div className="recenzija-ocjena">
                                    <p>Ocjena:</p>
                                    <p id="brojzvjezdica">{recenzija.recenzijaZvjezdice}</p>
                                </div>
                                <div>
                                    <p>Vrijeme kreiranja:</p>
                                    <p>{recenzija.vrijemeKreiranja}</p></div>
                                <div id="odobrenotekst-recenzija">
                                    {recenzija.odobrioModerator && (<p id="odebrenotekst">Odobrio moderator!</p>)}
                                </div>
                                <button className="brisanjeRec" onClick={() => brisanjeRecenzije(recenzija.recenzijaId)}>Izbriši recenziju!</button>
                            </div>
                        </div>
                        {
                            index !== recenzije.length - 1 && <hr />
                        }
                     </div>
                  )
                  )
               )
               
               :
               (<p>Korisnik nije ostavio recenziju.</p> )
              
            }

            </div>

         </div>
      </div>
   );
}