import { Navbar } from "../components/Navbar";
import React, { useEffect, useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import "../styles/PonudeiPromocije.css";
import QRCode from "qrcode";

export function PonudeiPromocije() {
   const [popusti, setPopusti] = useState([]);
   const [qrCodes, setQrCodes] = useState({});
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
      fetch(`/api/popusts`, options)
         .then((response) => {
         if (!response.ok) {
            throw new Error("Neuspješno dohvaćanje ponuda.");
         }
         return response.json();
      })
         .then((data) => {
            setPopusti(data);
            generateQRCodes(data);
      })
         .catch((error) => {
         setError(error.message);
      });
      

      
   }, []);

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
            {error ? (
               <p className="text-danger">{error}</p>
            ) : !popusti.length ? (
               <p>Loading...</p>
            ) : (
               <div className="popust-popust-row">
                  
                  <div className="banner">
                     Pronađite najbolje ponude i popuste uz samo par klikova!
                  </div>
                        
                  <div id="popusti" className="popusti-section">
                     {popusti.length > 0 ? (
                        popusti.map((pop)=>(
                           <div key={pop.popustId} className="my-popust-wrapper"> 
                              <div className="popust-card">
                                 <div className="popust-header">
                                    <p>{pop.popustNaziv}</p>
                                    <hr />
                                 </div>
                                 
                                 <div className="popust-body">
                                    <div className="popust-info">
                                       {/* <p className="popust-store">{pop.store}</p>*/}
                                       <p className="popust-details">{pop.popustOpis}</p>
                                       {/* <p className="popust-code">Kod: {pop.code}</p>  */}
                                    </div>
                                    <div className="popust-actions">
                                       
                                    {qrCodes[pop.popustId] && (
                                          <img
                                             src={qrCodes[pop.popustId]}
                                             alt="QR Code"
                                             className="qr-code"
                                          />
                                       )}
                                       <button className="save-button">Spremi ponudu</button> 
                                    </div>
                                 </div>
                              </div>
                           </div>
                        ))
                       ) : (
                        <p>No deals available.</p>      
                     )}
                  </div>
               </div>
               
            )}
         </div>
      </div>
   );
}