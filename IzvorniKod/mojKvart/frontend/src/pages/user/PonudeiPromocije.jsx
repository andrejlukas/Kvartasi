import { Navbar } from "../../components/Navbar";
import React, { useEffect, useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import "../../styles/PonudeiPromocije.css";
import QRCode from "qrcode";

export function PonudeiPromocije() {
   const [popusti, setPopusti] = useState([]);
   const [ponude, setPonude] = useState([]);
   const [email, setEmail]=useState('');
   const [idKupac, setIdKupac]=useState(null);
   const [qrCodes, setQrCodes] = useState({});
   const [error, setError] = useState(null);
   const [loading, setLoading]=useState(true);

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
          .then(async response => {
              if (!response.ok) {
                  const text = await response.text();
                 throw new Error(text);
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
            .then(async response => {
               
               if (!response.ok) {
                  const text = await response.text();
                  throw new Error(text);
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
      console.log(idKupac)
      const token = localStorage.getItem('token');
      const options = {
         method: 'GET',
         headers: {
         'Authorization': `Bearer ${token}`,
         'Content-Type': 'application/json',
         },
      };

      setLoading(true); 

      Promise.all([
      fetch(`/api/popusts/flag-true/${idKupac}`, options)
         .then((response) => {
         if (!response.ok) {
            throw new Error("Neuspješno dohvaćanje popusta.");
         }
         return response.json();
      })
         .then((data) => {
            console.log(data)
            setPopusti(data);
            generateQRCodes(data);
      })
         .catch((error) => {
         setError(error.message);
      }),
      fetch(`/api/ponudas/flag-true/${idKupac}`, options)
         .then((response) => {
         if (!response.ok) {
            throw new Error("Neuspješno dohvaćanje ponuda.");
         }
         return response.json();
      })
         .then((data) => {
            setPonude(data);
      })
   ])
         .catch((error) => {
         setError(error.message);
      })
      .finally(() => {
         setLoading(false); 
      });
      
   }, [idKupac]);

   const generateQRCodes = async (data) => {
      const qrCodePromises = data.map(async (popust) => {
         const url = await QRCode.toDataURL(popust.popustQrkod);
         return { id: popust.popustId, url };
      });

      const qrCodeResults = await Promise.all(qrCodePromises);
      const qrCodeMap = qrCodeResults.reduce((acc, { id, url }) => {
         acc[id] = url;
         return acc;
      }, {});

      setQrCodes(qrCodeMap);
   };


   return (
      <div>
         <Navbar/>
         <div className="container-popusti">
            { loading ? (
               <p>Loading...</p>
            ) : (popusti.length + ponude.length>0 ? (
               <div className="popust-popust-row">
                  <div className="banner">
                     Pronađite najbolje ponude i popuste uz samo par klikova!
                  </div>
                  <div className="ponuda-popust-wrapper"> 
                  <div id="popusti" className="popusti-section">
                     {popusti.length > 0 && (
                        popusti.map((pop) => (
                           <div key={pop.popustId} className="my-popust-wrapper">
                              <div className="popust-card">
                                 <div className="popust-header">
                                    <p>{pop.popustNaziv} - {pop.trgovinaIme}</p>                               
                                    <hr />
                                 </div>
                                 <div className="popust-body">
  
                                    <div className="popust-info">
                                       <p className="popust-details">{pop.popustOpis}</p>
                                    </div>
                                    <div className="popust-actions">
                                       {qrCodes[pop.popustId] && (
                                          <img
                                             src={qrCodes[pop.popustId]}
                                             alt="QR Code"
                                             className="qr-code"
                                          />
                                       )}
                                       <button className="save-button">Spremi popust</button>
                                    </div>
                                 </div>
                              </div>
                           </div>
                        ))
                     )}
                  </div>
                  <div id="ponude" className="popusti-section">
                     {ponude.length > 0 && (
                        ponude.map((ponuda) => (
                           <div key={ponuda.ponudaId} className="my-popust-wrapper">
                              <div className="popust-card">
                                 <div className="popust-header">
                                    <p>{ponuda.ponudaNaziv} - {ponuda.trgovinaIme}</p>
                                    <hr />
                                 </div>
                                 <div className="popust-body">
                                    <div className="popust-info">
                                       <p className="popust-details">{ponuda.ponudaOpis}</p>
                                    </div>
                                    <div className="popust-actions">
                                       <button className="save-button">Spremi ponudu</button>
                                    </div>
                                 </div>
                              </div>
                           </div>
                        ))
                     )}
                     </div>
                     </div>
                  </div>
               ) : (
               <p>Nema dostupnih popusta ili proizvoda</p>      
            ))}
         </div>
      </div>
   );
}