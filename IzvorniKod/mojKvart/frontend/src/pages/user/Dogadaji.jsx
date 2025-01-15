import { Navbar } from "../../components/Navbar";
import React, { useEffect, useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import "../../styles/Dogadaji.css";

export function Dogadaji() {
   const [kupacEmail, setKupacEmail] = useState("");
   const [kupacId, setKupacId] = useState(null);
   const [kupacAtending, setKupacAtending] = useState([]);
   const [dogadaji, setDogadaji] = useState([]);
   const [trgovinaNames, setTrgovinaNames] = useState({});
   const [error, setError] = useState(null);
   
   useEffect(() => {
      const token = localStorage.getItem('token');
      const options1 = {
         method: 'POST',
         headers: {
         'Authorization': `Bearer ${token}`,
         'Content-Type': 'application/json',
         }, body: JSON.stringify( { oneLiner: token })
      };
      fetch('/api/tokens/claims', options1)
        .then(async response => {
            if (!response.ok) {
              const text = await response.text();
              throw new Error(text);
            }
            return response.json();
        })
        .then(data => {
            setKupacEmail(data.email);
        })
        .catch(error => setError(error.message));

      const options2 = {
         method: 'GET',
         headers: {
         'Authorization': `Bearer ${token}`,
         'Content-Type': 'application/json',
         },
      };
      fetch(`/api/dogadajs`, options2)
         .then((response) => {
            if (!response.ok) {
               throw new Error("Neuspješno dohvaćanje događaja.");
            }
            return response.json();
         }).then((data) => {
            setDogadaji(data);
            data.forEach(dog => fetchTrgovinaName(dog.trgovina));
         }).catch((error) => {
            setError(error.message);
         });
      
      const fetchTrgovinaName = async (trgovinaId) => {
         try {
            const options3 = {
               method: 'GET',
               headers: {
               'Authorization': `Bearer ${token}`,
               'Content-Type': 'application/json',
               },
            };
            const response = await fetch(`/api/trgovinas/getById/${trgovinaId}`, options3);
            if (!response.ok) {
               throw new Error(`Failed to fetch store with id ${trgovinaId}`);
            }
            const name = await response.json();
            setTrgovinaNames(prevNames => ({ ...prevNames, [trgovinaId]: name.trgovinaNaziv }));
         } catch (error) {
            console.error(`Error fetching store name for id ${trgovinaId}:`, error);
         }
      };
   }, []);

   useEffect(() => {
      const token = localStorage.getItem('token');
      let options = {
         method: 'GET',
         headers: {
         'Authorization': `Bearer ${token}`,
         'Content-Type': 'application/json',
         },
      };
      
      if(kupacEmail) {
         fetch(`/api/kupacs/${kupacEmail}`, options)
         .then(async response => {
            if (!response.ok) {
               const text = await response.text();
               throw new Error(text);
            }
            return response.json();
         })
         .then(data => {
            setKupacId(data.kupacId);
         })
         .catch(error => setError("Neuspješno dohvaćanje podataka o kupcu"));
      }
   }, [kupacEmail]);

   useEffect(() => {
      if(!kupacId) return;

      const token = localStorage.getItem('token');
      const options = {
         method: 'GET',
         headers: {
         'Authorization': `Bearer ${token}`,
         'Content-Type': 'application/json',
         },
      };

      fetch(`/api/kupacDogadajs/kupac/${kupacId}`, options)
         .then(async response => {
            if (!response.ok) {
               const text = await response.text();
               throw new Error(text);
            }
            return response.json();
         })
         .then(data => {
            setKupacAtending(data.map(kd => kd.dogadaj));
         })
         .catch(error => setError(error.message));
   }, [kupacId]);

   const comingToEvent = (dogadajId) => {
      const token = localStorage.getItem('token');
      const options = {
         method: 'POST',
         headers: {
         'Authorization': `Bearer ${token}`,
         'Content-Type': 'application/json',
         }, body: JSON.stringify({ kupacDogadajFlag: true, kupac: kupacId, dogadaj: dogadajId })
      };
      fetch(`/api/kupacDogadajs`, options)
         .then(async response => {
            if (!response.ok) {
               const text = await response.text();
               throw new Error(text);
            }
            return response.json();
         })
         .then(resp => {
            window.location.reload();
         })
         .catch(error => setError(error.message));
   };



   return (
      <div>
         <Navbar/>
         <div className="container-dogadaji">
            {error ? (
               <p className="text-danger">{error}</p>
            ) : !dogadaji || !kupacEmail || !kupacAtending ? (
               <p>Loading...</p>
            ) : (
               <div className="dogadaj-dogadaj-row">
                  
                  <div className="filteri-dogadaji">
                           <p>filteri1</p>
                           <p>filter2 </p>
                  </div>
                        
                  <div className="dogadaji-section">
                     <div className="row-dogadaji">
                        {dogadaji.length > 0 ? (
                           dogadaji.map((dog)=>(
                              <div key={dog.dogadajId} className="my-dogadaj-wrapper"> 
                                 <div className="dogadaj-card">
                                    <img src={dog.dogadajSlika} alt={dog.dogadajNaziv} />

                                    <div className="card-body-dogadaj">
                                       <div className="card-title"> {dog.dogadajNaziv}</div>
                                       <div className="items-dogadaji">
                                          <div className="card-text">{trgovinaNames[dog.trgovina]}</div>
                                          {kupacAtending.indexOf(dog.dogadajId) === -1 ?
                                             (<button className="confirm-button" onClick={() => comingToEvent(dog.dogadajId)}>Potvrdi dolazak</button>) :
                                             (<button className="confirm-button" id="replyOver">Dolazim!</button>)
                                          }
                                       </div>
                                       <div className="card-text"> {dog.dogadajOpis}</div>
                                       
                                       <div className="card-text"> {"Početak: " + dog.dogadajPocetak}</div>
                                          
                                       
                                    </div>
                                 </div>
                              </div>
                           ))
                        ) : (
                           <p>Nema dostupnih događaja.</p>      
                        )}
                     </div>
                  </div>
               </div>
               
            )}

         </div>
      </div>
   );
}