import { Navbar } from "../../components/Navbar";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { MapContainer, TileLayer, Marker, useMapEvents  } from 'react-leaflet';
import "leaflet/dist/leaflet.css";
import "../../styles/MojiRacuni.css"; 

export function MojiRacuni() {
    
    const [racuni,setRacuni] = useState({})
    const [trgovine,setTrgovine] = useState([])

    //recenzija
    const [tekstRec,setTekstRec] = useState("")
    const [ocjenaRec,setOcjenaRec] = useState(1)
    const [trgovinaRec,setTrgovinaRec] = useState("")

    const [racuniInfo,setRacuniInfo] = useState({})
    const [error,setError] = useState(null)

    //potrebno za ispis proizvoda u računu
    const [openedRacuni, setOpenedRacuni] = useState([]); // Čuva ID-ove otvorenih računa
    const [openedRecenzije, setOpenedRecenzije] = useState([]); // Čuva ID-ove računa otvorenih recenzija


    const prikazracuna = (key) => {
        if (openedRacuni.includes(key)) {
            // Ako je već otvoren, ukloni ga iz niza
            setOpenedRacuni(openedRacuni.filter((id) => id !== key));
            setOpenedRecenzije(openedRacuni.filter((id) => id !== key));
        } else {
            // Ako nije otvoren, dodaj ga u niz
            setOpenedRacuni([...openedRacuni, key]);
        }
    };

    const ostaviRecenziju = (key) => {
        if (openedRecenzije.includes(key)) {
            // Ako je već otvoren, ukloni ga iz niza
            setOpenedRecenzije(openedRacuni.filter((id) => id !== key));
        } else {
            // Ako nije otvoren, dodaj ga u niz
            setOpenedRecenzije([...openedRacuni, key]);
        }
    };

    function dodajRec (e,poslaID)

    {
        console.log("unutra sa,")
        e.preventDefault();

        const trenutnoVrijeme = new Date();
        const formatiranoVrijeme = trenutnoVrijeme.toISOString().slice(0, 19);
        console.log(formatiranoVrijeme)
        if(!formatiranoVrijeme || !ocjenaRec || !tekstRec || !id)
            setError("nije dobro za dat rec")
            

        const token = localStorage.getItem('token');
        
            if (!token || !poslaID) {
                throw new Error("Podaci za recenziju nisu dostupni");
              }
          
            const options4 = {
                method: "POST",
                headers: {
                  Authorization: `Bearer ${token}`,
                  "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    odobrioModerator: false,
                    recenzijaOdgovor: null, 
                    vrijemeKreiranja: "2025-01-09T10:00:00",
                    recenzijaZvjezdice: ocjenaRec ,
                    recenzijaOpis: tekstRec,
                    kupacId: id,
                    trgovinaId:poslaID,

                  }),
              };
            fetch(`/api/recenzijas`, options4)
            .then(response => response.ok ? response.json() : Promise.reject('Failed to save changes'))
            .then(updated => {
                console.log(updated)
            })
            .catch(error => {
                console.error('Error updating data:', error);
            })

       

    }






    //dohvacanje racuna - imam samo id kupca
    const [id, setId] = useState("")
    const [emailAddress, setEmailAddress] = useState('')

       

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
    console.log(token)

    fetch('/api/tokens/claims', options)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
          setEmailAddress(data.email);
          //console.log(data)
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
     
        // Dohvaćanje raucna preko id-a
        //console.log(id)
        if (id) {
            fetch(`api/kupacProizvods/prosleNarudzbe/${id}`, options)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(data => {
                    console.log(data)
                    setRacuni(data)
                    Object.keys(data).forEach((key) => fetchRacunInfo(key));

                })
                .catch(error => {
                    console.error('There was a problem with the fetch operation: ', error);
                    setError(error.message);
                });
        }

        const fetchRacunInfo = async (racunId) => {
            try {
               const options3 = {
                  method: 'GET',
                  headers: {
                  'Authorization': `Bearer ${token}`,
                  'Content-Type': 'application/json',
                  },
               };
               const response = await fetch(`/api/racuns/${racunId}`, options3);
               if (!response.ok) {
                  throw new Error(`Failed to fetch racun with id ${racunId}`);
               }
               const name = await response.json();
               //console.log(name)
               setRacuniInfo(prevNames => ({ ...prevNames, [racunId]: [name.trgovina,name.vrijemeDatumNastanka] }));
            } catch (error) {
               console.error(`Error fetching store name for id ${racunId}:`, error);
            }
         };
     }, [id]);

    useEffect(() => console.log(racuniInfo), [racuniInfo])

     // koristit
        /*  <button onClick={handleToggle}>
        {showDiv ? "Ukloni tekst" : "Dodaj tekst"}
    </button>
    {showDiv && <div className="textDiv">Ovo je tekst unutar stvorenog diva.</div>}
    */
   return (
      <div className="moji-racuni-wrapper">

         <Navbar/>
        
         <div className="main-container">
                    {
                        
                        
                        
                            <div className="moji-racuni-container">
                                <h1>Moji racuni</h1>
                                <div className="moji-racuni-lista">
                                    {
                                        Object.keys(racuni).length > 0 ? (Object.keys(racuni).map((key) => (
                                            //iteriram po ključevima(idračuna)
                                            
                                            <div key={key} className="racun-element">
                                               {
                                                Object.entries(racuniInfo).map(([key2, podaci]) =>
                                                    key2 === key ? (
                                                    <button
                                                        key={key2} // Obavezno dodajte jedinstveni `key` za svaki element
                                                        className="idracun-button"
                                                        onClick={() => prikazracuna(key)}
                                                    >
                                                        Račun: {key} : {racuni[key][0].trgovinaNaziv} : {racuniInfo[key][1]}
                                                    </button>
                                                    ) : null // Ako uvjet nije ispunjen, vraća `null` (ništa)
                                                )
                                                }
                                                
                                                
                                                {openedRacuni.includes(key) && <div className="ispisRacuna">

                                                    <button className="racun-ostavi-recenziju-gumb" onClick={() => ostaviRecenziju(key)} >Ostavi recenziju!</button>
                                                    {
                                                        openedRecenzije.includes(key) && 
                                                        (
                                                            <div className="ostavljanje-recenzije-container">
                                                                <p>Ostavi recenziju</p>
                                                                <form id ="unos-recenziije-form" onSubmit={(e) => dodajRec(e, racuniInfo[key][0])} >
                                                                <div>
                                                                    <label >Unesite tekst:</label>
                                                                    <input id="tekstrecenzija-mojiracuni"
                                                                        type="text"
                                                                        placeholder={tekstRec}
                                                                        value={tekstRec}
                                                                        onChange={(e) => setTekstRec(e.target.value)}
                                                                        required
                                                                    />
                                                                </div>
                                
                                                                <div>
                                                                    <label htmlFor="numberInput">Ocjeni:</label>
                                                                    <input
                                                                        type="number"
                                                                        min="1"
                                                                        max="5"
                                                                        value={ocjenaRec}
                                                                        onChange={(e) => setOcjenaRec(Number(e.target.value))}
                                                                        required
                                                                    />
                                                                </div>
                                
                                                                <div className="form-buttons">
                                                                    <button  type="submit" >Predaj</button>
                                                                </div>
                                
                                                                </form>
                                                                
                                                            </div>
                                
                                                            
                                                        )
                                                    }
                                                    <hr></hr>
                                                    <div className="racun-dopunski-tekst">
                                                        <p>Proizvod</p>
                                                        <p>Cijena</p>
                                                        <p>Kolicina</p>
                                                        <p>Ukupna cijena:</p>
                
                                                    </div >
                                                    
                                                    {
                                                         // Dohvaća vrijednost ključa (niz proizvoda)
                                                        racuni[key].map((proizvod) => (
                                                            /* console.log(`Proizvod: ${proizvod.proizvodNaziv}`);
                                                            console.log(`Cijena: ${proizvod.proizvodCijena}`);
                                                            console.log(`Trgovina: ${proizvod.proizvodKolicina}`); */
                                                            <div key={proizvod.proizvodNaziv} className="racun-proizvod-element">
                                                                <p>{proizvod.proizvodNaziv}</p>
                                                                <p>{proizvod.proizvodCijena}</p>
                                                                <p>{proizvod.proizvodKolicina}</p>
                                                                <p>{proizvod.proizvodKolicina * proizvod.proizvodCijena}</p>
                                                                
                                                            </div>
                                                            
                                                        )
                                                        )
                                                    }
                                                    
                                                    
                                                </div>}
                                            </div>
                                        )))    
                                    :
                                    (<p>Korisnik nema račune</p>)
                                    }

                                </div>
                            </div>
                            
                                
                        
                    }
                
            </div>
      </div>
   );
}