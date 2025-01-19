import { Navbar } from "../../components/Navbar";
import { useState, useEffect } from "react";

import '../../styles/MojePonudeiPromocije.css'


export function MojePonudeiPromocije(){
    const [ponude,setPonude] = useState([])
    const [popusti,setPopusti] = useState([])
    const [email, setEmail] = useState("")
    const [idKupac, setIdKupac]=useState(null);
    const [error,setError] = useState("")
    const [loading,setLoading] = useState(true)

    const [message,setMessage] =useState("")

    function zatvori() {
        setMessage("")
    }

    async function iskoristiPonudu(kupacPonudaPopustId, idponude) {
        if (!kupacPonudaPopustId) {
            setError("Nemam podatke za iskorištiti ponudu");
            console.error("Nemam podatke za iskorištiti ponudu");
            return;
        }
    
        const token = localStorage.getItem('token');
        if (!token) {
            setError("Token nije pronađen");
            console.error("Token nije pronađen u localStorage");
            return;
        }
    
        const optionsPUT = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({
                novoStanje: true
            })
        };
    
        const optionsGET = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
        };
    
        try {
            const putResponse = await fetch(`/api/kupacPonudaPopusts/stanje/${kupacPonudaPopustId}`, optionsPUT);
            
            if (!putResponse.ok) {
                const errorText = await putResponse.text();
                throw new Error(`PUT zahtjev nije uspio: ${errorText}`);
            }
    
            setMessage(`Ponuda ${idponude} iskorištena`);
    
            const getResponse = await fetch(`/api/kupacPonudaPopusts/ponude/neiskoristene/${idKupac}`, optionsGET);
    
            if (!getResponse.ok) {
                const errorText = await getResponse.text();
                throw new Error(`GET zahtjev nije uspio: ${errorText}`);
            }
    
            const data = await getResponse.json();
            setPonude(data);
    
        } catch (error) {
            console.error("Greška u funkciji iskoristiPonudu:", error);
            setError(error.message || "Došlo je do nepoznate greške");
        }
    }

    async function iskoristiPopust(kupacPonudaPopustId2, idponude) {
        if (!kupacPonudaPopustId2) {
            setError("Nemam podatke za iskorištiti popust");
            console.error("Nemam podatke za iskorištiti popust");
            return;
        }
    
        const token = localStorage.getItem('token');
        if (!token) {
            setError("Token nije pronađen");
            console.error("Token nije pronađen u localStorage");
            return;
        }
    
        const optionsPUT = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({
                novoStanje: true
            })
        };
    
        const optionsGET = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
        };
    
        try {
            const putResponse = await fetch(`/api/kupacPonudaPopusts/stanje/${kupacPonudaPopustId2}`, optionsPUT);
            
            if (!putResponse.ok) {
                const errorText = await putResponse.text();
                throw new Error(`PUT zahtjev nije uspio: ${errorText}`);
            }
    
            setMessage(`Popust ${idponude} iskorišten`);
    
            const getResponse = await fetch(`/api/kupacPonudaPopusts/popusti/neiskoristeni/${idKupac}`, optionsGET);
    
            if (!getResponse.ok) {
                const errorText = await getResponse.text();
                throw new Error(`GET zahtjev nije uspio: ${errorText}`);
            }
    
            const data = await getResponse.json();
            setPopusti(data);
    
        } catch (error) {
            console.error("Greška u funkciji iskoristiPopust:", error);
            setError(error.message || "Došlo je do nepoznate greške");
        }
    }
    


    useEffect(() => {
          const token = localStorage.getItem('token');
          const options = {
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
                      return response.text().then(text => {throw new Error(text)});
                  }
                  return response.json();
              })
              .then(data => {
                  setEmail(data.email);
              })
              .catch(error => setError(error.message));
      }, []);

    useEffect(() => {
        const token = localStorage.getItem('token');
        const options = {
            method: 'GET',
            headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
            },
        };
      
        if (email) {
            fetch(`/api/kupacs/${email}`, options)
                  .then(response => {
                     
                     if (!response.ok) {
                        return response.text().then(text => {throw new Error(text)});
                     }
                     return response.json();
                  })
                  .then(data => {
                     setIdKupac(data.kupacId);
                  })
                  .catch(error => setError(error.message));
        }
    }, [email]);

    useEffect(() => {
        const token = localStorage.getItem('token');
        const options = {
            method: 'GET',
            headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
            },
        };
        setLoading(true)
      Promise.all([
        fetch(`/api/kupacPonudaPopusts/ponude/neiskoristene/${idKupac}`, options)
                  .then(response => {
                     
                     if (!response.ok) {
                        return response.text().then(text => {throw new Error(text)});
                     }
                     return response.json();
                  })
                  .then(data => {
                     setPonude(data);
                  })
                  .catch(error => setError(error.message)),
                  fetch(`/api/kupacPonudaPopusts/popusti/neiskoristeni/${idKupac}`, options)
                  .then(response => {
                     
                     if (!response.ok) {
                        return response.text().then(text => {throw new Error(text)});
                     }
                     return response.json();
                  })
                  .then(data => {
                     setPopusti(data);
                  })
                  .catch(error => setError(error.message))

      ])
      .catch((error) => {
        setError(error.message);
     })
     .finally(() => {
        setLoading(false); 
     });
        if (email) {
            
        }
    }, [idKupac]);

   
    
  

      

   /*  useEffect(()=>{ console.log(ponudapopust)}, [ponudapopust]
   ) */
   


    return(
        <div className="moje-ponude-promocije-wrappper">
            <Navbar/>
            <div className="main-container">
                    {
                        loading ? (<p>Loading...</p>) : (
                            <div className="moje-ponude-i-promocije-wrapper">
                    
                                <div className="kosarica-odabir">
                                <ul >
                                    <li><a id="kosarica-odabir"href="/kosarica">Moja košarica</a></li>
                                    <li><a id="ponude-i-promocije-odabir"href= "/mojeponudeipromocije">Moje ponude i promocije</a></li>
                                </ul>
                                </div>
                                
                                <div className="ispis-mojih-ponuda-i-promocija">
                                <div className="moji-neiskoristeni-popusti">
                                    {
                                        popusti && popusti.map((element)=>
                                        (
                                            <div id="moj-popust" key={element.ponudaPopustId}>
                                                <div className="moje-ponude-popusti-info">
                                                <p>{element.popustNaziv }</p>
                                                <p>  {element.trgovinaIme}</p>
                                                <p>{element.popustOpis}</p>
                                                {/* <p>Popust: {element.popustId}</p> */}

                                                </div>
                                                <div className="moje-ponude-popusti-buttons" >
                                                <button >Makni popust</button>
                                                <button onClick={()=> iskoristiPopust(element.kupacPonudaPopustId,element.popustId)}>Iskoristio popust</button>

                                                </div>
                                               
                                                
                                            </div>
                                        )
                                        )
                                    }
                                </div>
                                <div className="moje-neiskoristene-ponude">
                                    {
                                        ponude && ponude.map((element)=>
                                        (
                                            <div id="moja-ponuda" key={element.ponudaPopustId}>
                                                <div className="moje-ponude-popusti-info">
                                                <p>{element.ponudaNaziv }</p>
                                                <p> {element.trgovinaIme}</p>
                                                <p>{element.ponudaOpis}</p>
                                                {/* <p> Ponuda:  {element.ponudaId}</p> */}
                                                </div>
                                                <div className="moje-ponude-popusti-buttons">
                                                <button >Makni ponudu</button>
                                                <button onClick={()=> iskoristiPonudu(element.kupacPonudaPopustId,element.ponudaId)}>Iskoristio ponudu</button>

                                                </div>
                                                



                                            </div>
                                        )
                                        )
                                    }
                                </div>
                                </div>
                            </div>
                        )
                    }
                
                
            </div>
            {
                        message  && <div className="moje-ponude-popusti-zatvori-div"><p>{message}</p> <button className="zatvaranje-obavijesti-o-iskoristenom" onClick={() => zatvori()}>Zatvori</button></div>
                    }
            
        </div>
    )
    
}