import { Navbar } from "../../components/Navbar";
import { useState, useEffect } from "react";

import '../../styles/MojePonudeiPromocije.css'


export function MojePonudeiPromocije(){
    const [ponudapopust,setponudaPopust] = useState([])
    const [email, setEmail] = useState("")
    const [idKupac, setIdKupac]=useState(null);
    const [error,setError] = useState("")


    


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
            fetch(`/api/kupacPonudaPopusts`, options)
                  .then(response => {
                     
                     if (!response.ok) {
                        return response.text().then(text => {throw new Error(text)});
                     }
                     return response.json();
                  })
                  .then(data => {
                    //console.log(data)
                    data.forEach(element => {
                        if (element.kupac === idKupac) { // Provjera uvjeta
                            fetchPonudaiPromocija(element.ponudaPopust); // Pozovi samo ako uvjet odgovara
                        }
                    });
                })
                  .catch(error => setError(error.message));
        }

        const fetchPonudaiPromocija = async (ponudaPopust) => {
            //console.log(ponudaPopust)
            try {
               const response = await fetch(`/api/ponudaPopusts/${ponudaPopust}`, options);
               if (!response.ok) {
                  throw new Error(`Failed to fetch ponudapopust with id ${ponudaPopust}`);
               }
               const idponudapopust = await response.json();
               setponudaPopust(prev => [...prev, idponudapopust]);
            } catch (error) {
               console.error(`Error fetching store name for id ${ponudaPopust}:`, error);
            }
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
                    
                    <h1>naslov</h1>
                    <div>
                        {
                            ponudapopust.map((element)=>
                            (
                                <div key={element.ponudaPopustId}>
                                    <p>ponuda popust flag: {element.ponudaPopustFlag ? 1 : 0}</p>
                                    <p>ponudapopust id : {element.ponudaPopustId}</p>
                                    <p>trgovina id : {element.trgovina}</p>
                                </div>
                            )
                            )
                        }
                    </div>

                </div>
                
            </div>
            
        </div>
    )
    
}