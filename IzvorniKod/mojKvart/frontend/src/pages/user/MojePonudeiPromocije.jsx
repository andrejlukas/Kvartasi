import { Navbar } from "../../components/Navbar";
import { useState, useEffect } from "react";

import '../../styles/MojePonudeiPromocije.css'


export function MojePonudeiPromocije(){
    const [ponude,setPonude] = useState([])
    const [popusti,setPopusti] = useState([])
    const [email, setEmail] = useState("")
    const [idKupac, setIdKupac]=useState(null);
    const [error,setError] = useState("")

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
            console.log(`Slanje PUT zahtjeva na /api/kupacPonudaPopusts/stanje/${kupacPonudaPopustId}`);
            const putResponse = await fetch(`/api/kupacPonudaPopusts/stanje/${kupacPonudaPopustId}`, optionsPUT);
            
            if (!putResponse.ok) {
                const errorText = await putResponse.text();
                throw new Error(`PUT zahtjev nije uspio: ${errorText}`);
            }
    
            console.log(`PUT zahtjev uspješan za ponudu ID: ${idponude}`);
            setMessage(`Ponuda ${idponude} iskorištena`);
    
            console.log(`Slanje GET zahtjeva na /api/kupacPonudaPopusts/ponude/neiskoristene/${idKupac}`);
            const getResponse = await fetch(`/api/kupacPonudaPopusts/ponude/neiskoristene/${idKupac}`, optionsGET);
    
            if (!getResponse.ok) {
                const errorText = await getResponse.text();
                throw new Error(`GET zahtjev nije uspio: ${errorText}`);
            }
    
            const data = await getResponse.json();
            setPonude(data);
            console.log("Dobiveni podaci za ponude:", data);
    
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
            console.log(kupacPonudaPopustId2)
            console.log(`Slanje PUT zahtjeva na /api/kupacPonudaPopusts/stanje/${kupacPonudaPopustId2}`);
            const putResponse = await fetch(`/api/kupacPonudaPopusts/stanje/${kupacPonudaPopustId2}`, optionsPUT);
            
            if (!putResponse.ok) {
                const errorText = await putResponse.text();
                throw new Error(`PUT zahtjev nije uspio: ${errorText}`);
            }
    
            console.log(`PUT zahtjev uspješan za popust ID: ${idponude}`);
            setMessage(`Popust ${idponude} iskorišten`);
    
            console.log(`Slanje GET zahtjeva na /api/kupacPonudaPopusts/popusti/neiskoristeni/${idKupac}`);
            const getResponse = await fetch(`/api/kupacPonudaPopusts/popusti/neiskoristeni/${idKupac}`, optionsGET);
    
            if (!getResponse.ok) {
                const errorText = await getResponse.text();
                throw new Error(`GET zahtjev nije uspio: ${errorText}`);
            }
    
            const data = await getResponse.json();
            setPopusti(data);
            console.log("Dobiveni podaci za popust:", data);
    
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
      
        if (email) {
            fetch(`/api/kupacPonudaPopusts/ponude/neiskoristene/${idKupac}`, options)
                  .then(response => {
                     
                     if (!response.ok) {
                        return response.text().then(text => {throw new Error(text)});
                     }
                     return response.json();
                  })
                  .then(data => {
                     setPonude(data);
                     console.log(data)
                  })
                  .catch(error => setError(error.message));
        }
    }, [idKupac]);

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
            fetch(`/api/kupacPonudaPopusts/popusti/neiskoristeni/${idKupac}`, options)
                  .then(response => {
                     
                     if (!response.ok) {
                        return response.text().then(text => {throw new Error(text)});
                     }
                     return response.json();
                  })
                  .then(data => {
                     setPopusti(data);
                     console.log(data)
                  })
                  .catch(error => setError(error.message));
        }
    }, [idKupac]);

    
  

      

   /*  useEffect(()=>{ console.log(ponudapopust)}, [ponudapopust]
   ) */
   


    return(
        <div className="moje-ponude-promocije-wrappper">
            <Navbar/>
            <div className="main-container">
                <div className="moje-ponude-i-promocije-wrapper">
                    <div className="kosarica-odabir">
                    <ul >
                        <li><a href="/kosarica">Moja košarica</a></li>
                        <li><a id="ponude-i-promocije-odabir"href= "/mojeponudeipromocije">Moje ponude i promocije</a></li>
                    </ul>
                    </div>
                    
                    <div className="ispis-mojih-ponuda-i-promocija">
                    <div className="moji-neiskoristeni-popusti">
                        {
                            popusti && popusti.map((element)=>
                            (
                                <div key={element.ponudaPopustId}>
                                    <p>Popust: {element.popustId}</p>
                                    <p>  {element.popustNaziv }</p>
                                    <p>  {element.popustOpis}</p>
                                    <p>  {element.trgovinaIme}</p>
                                    <button >Makni popust</button>
                                    <button onClick={()=> iskoristiPopust(element.kupacPonudaPopustId,element.popustId)}>Iskoristio popust</button>
                                </div>
                            )
                            )
                        }
                    </div>
                    <div className="moje-neiskoristene-ponude">
                        <div>
                        {
                            ponude && ponude.map((element)=>
                            (
                                <div key={element.ponudaPopustId}>
                                    <p> Ponuda:  {element.ponudaId}</p>
                                    <p>  {element.ponudaNaziv }</p>
                                    <p>  {element.ponudaOpis}</p>
                                    <p> {element.trgovinaIme}</p>
                                    <button >Makni ponudu</button>
                                    <button onClick={()=> iskoristiPonudu(element.kupacPonudaPopustId,element.ponudaId)}>Iskoristio ponudu</button>



                                </div>
                            )
                            )
                        }
                        </div>
                    </div>
                    </div>
                    
                    
                    
                    

                </div>
                
            </div>
            {
                        message  && <div className="moje-ponude-popusti-zatvori-div"><p>{message}</p> <button className="zatvaranje-obavijesti-o-iskoristenom" onClick={() => zatvori()}>Zatvori</button></div>
                    }
            
        </div>
    )
    
}