import { Navbar } from "../../components/Navbar";
import React, { useEffect, useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import "../../styles/Dogadaji.css";

export function Dogadaji() {
   const [dogadaji, setDogadaji] = useState([]);
   const [trgovinaNames, setTrgovinaNames] = useState({});
   const [error, setError] = useState(null);
   
   useEffect(() => {
      const token = localStorage.getItem('token');
      const options = {
         method: 'GET',
         headers: {
         'Authorization': `Bearer ${token}`,
         'Content-Type': 'application/json',
         },
      };
      fetch(`/api/dogadajs`, options)
         .then((response) => {
         if (!response.ok) {
            throw new Error("Neuspješno dohvaćanje ponuda.");
         }
         return response.json();
      })
         .then((data) => {
            setDogadaji(data);
            data.forEach(dog => fetchTrgovinaName(dog.trgovina));
            
      })
         .catch((error) => {
         setError(error.message);
         });
      
         const fetchTrgovinaName = async (trgovinaId) => {
      // console.log(trgovinaId)
      try {
         const response = await fetch(`/api/trgovinas/getById/${trgovinaId}`, options);
         if (!response.ok) {
            throw new Error(`Failed to fetch store with id ${trgovinaId}`);
         }
         const name = await response.json();
         // console.log(name.trgovinaNaziv)
         setTrgovinaNames(prevNames => ({ ...prevNames, [trgovinaId]: name.trgovinaNaziv }));
      } catch (error) {
         console.error(`Error fetching store name for id ${trgovinaId}:`, error);
      }
   };
      
   }, []);



   return (
      <div>
         <Navbar/>
         <div className="container-dogadaji">
            {error ? (
               <p className="text-danger">{error}</p>
            ) : !dogadaji ? (
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
                                          <button className="confirm-button">Dolazim</button>
                                       </div>
                                       <div className="card-text"> {dog.dogadajOpis}</div>
                                       
                                       <div className="card-text"> {dog.dogadajVrijeme}</div>
                                          
                                       
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