import { Navbar } from "../../components/Navbar";
import React, { useEffect, useState } from "react";
import { gapi } from "gapi-script";
import "bootstrap/dist/css/bootstrap.min.css";
import "../../styles/Dogadaji.css";

export function Dogadaji() {
   const [kupacEmail, setKupacEmail] = useState("");
   const [kupacId, setKupacId] = useState(null);
   const [kupacAtending, setKupacAtending] = useState([]);
   const [dogadaji, setDogadaji] = useState([]);
   const [trgovinaNames, setTrgovinaNames] = useState({});
   const [error, setError] = useState(null);
   const [chosenEvent, setChosenEvent] = useState(null);
   const [popupPhase, setPopupPhase] = useState("1");
   const [calendarLink, setCalendarLink] = useState(null);
   
   const CLIENT_ID = import.meta.env.VITE_GOOGLE_CALENDAR_CLIENT_ID;
   const API_KEY = import.meta.env.VITE_GOOGLE_CALENDAR_API_KEY;
   const DISCOVERY_DOCS = ["https://www.googleapis.com/discovery/v1/apis/calendar/v3/rest"]
   const SCOPES = "https://www.googleapis.com/auth/calendar.events";

   const [tokenClient, setTokenClient] = useState(null);
   const [gapiInited, setGapiInited] = useState(false);
   const [gisInited, setGisInited] = useState(false);

   useEffect(() => {
      gapi.load('client', gapiInit);
      gisLoad();
   }, [])

   useEffect(() => {
      if(localStorage.getItem("dogadajFilter") === null)
         localStorage.setItem("dogadajFilter", "N");
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
      if(localStorage.getItem("dogadajFilter") === null || localStorage.getItem("dogadajFilter") === "N")
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
      else
         fetch(`/api/dogadajs/filter`, options2)
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

   async function gapiInit() {
      await gapi.client.init({
        apiKey: API_KEY,
        discoveryDocs: DISCOVERY_DOCS,
      });
      setGapiInited(true);
   }

   function gisLoad() {
      setTokenClient(google.accounts.oauth2.initTokenClient({
        client_id: CLIENT_ID,
        scope: SCOPES,
        callback: '',
      }));
      setGisInited(true);
   }

   async function insertEvent(event) {
      let response;
      try {
         response = await gapi.client.calendar.events.insert({
            calendarId: 'primary',
            resource: event,
         });
      } catch (err) {
        document.getElementById('content').innerText = err.message;
        return;
      }
      setCalendarLink(response.result.htmlLink);
      setPopupPhase("3");
   }

   const parseStringToISOString = (dateString) => {
      const [datePart, timePart] = dateString.split(" ");
      const [day, month, year] = datePart.split(".").map(Number);
      const [hours, minutes] = timePart.split(":").map(Number);
      const date = new Date(year, month - 1, day, hours, minutes);
      return date.toISOString();
   };

   const comingToEventCheck = (dogadaj) => {
      setChosenEvent(dogadaj);
      document.getElementById("confirmDogadaj").style.display = "flex";
   };

   const comingToEvent = (dogadaj) => {
      const event = {
         summary: dogadaj.dogadajNaziv,
         description: dogadaj.dogadajOpis,
         start: {
           dateTime: parseStringToISOString(dogadaj.dogadajPocetak),
           timeZone: "Europe/Zagreb",
         },
         end: {
           dateTime: parseStringToISOString(dogadaj.dogadajKraj),
           timeZone: "Europe/Zagreb",
         },
         attendees: [{ email: kupacEmail }],
         reminders: {
            useDefault: false,
            overrides: [{method: "email", minutes: 24 * 60}]
         }
      };

      const token = localStorage.getItem('token');
      const options = {
         method: 'POST',
         headers: {
         'Authorization': `Bearer ${token}`,
         'Content-Type': 'application/json',
         }, body: JSON.stringify({ kupacDogadajFlag: true, kupac: kupacId, dogadaj: dogadaj.dogadajId })
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
            setPopupPhase("2");
            if (gapiInited && gisInited) {
               tokenClient.callback = async (resp) => {
                  if (resp.error !== undefined) throw resp;
                  await insertEvent(event);
               };
      
               if (gapi.client.getToken() === null) {
                  tokenClient.requestAccessToken({prompt: 'consent'});
               } else {
                  tokenClient.requestAccessToken({prompt: ''});
               }
            } else {
               throw new Error('Google API client or Identity Service is not properly initialized.');
            }
         })
         .catch(error => setError(error.message));
   };   

   const openTheFilter = () => {
      localStorage.setItem("dogadajFilter", "Y");
      window.location.reload();
   };

   const resetFilters = () => {
      localStorage.setItem("dogadajFilter", "N");
      window.location.reload();
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
                  
                  <div className="filters">
                     <strong><p>FILTRIRAJ</p></strong>
                     <button onClick={() => openTheFilter()}>Po najbrže nadolazećim</button>
                     <button onClick={() => resetFilters()}>Po početnim postavkama</button>
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
                                             (<button className="confirm-button" onClick={() => comingToEventCheck(dog)}>Potvrdi dolazak</button>) :
                                             (<button className="confirm-button" id="replyOver">Dolazim!</button>)
                                          }
                                       </div>
                                       <div className="card-text" id="pom"> {dog.dogadajOpis}</div>
                                       
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

         <div id="confirmDogadaj" className="filterPopovers">
            {popupPhase === "1" && <p>Želite li potvrditi dolazak na događaj</p>}
            {popupPhase === "1" && chosenEvent && <p>"{chosenEvent.dogadajNaziv}"?</p>}
            {popupPhase === "1" && 
               <div className="YesNoButtons">
                  <button onClick={() => comingToEvent(chosenEvent)}>Da</button>
                  <button onClick={() => document.getElementById("confirmDogadaj").style.display = "none"}>Ne</button>
               </div>}

            {popupPhase === "2" && <p>Vaš dolazak je evidentiran.</p>}
            {popupPhase === "2" && <p>Možete dodati ovaj događaj u Google kalendar.</p>}
            {popupPhase === "2" && 
               <div className="YesNoButtons">
                  <button onClick={() => window.location.reload()}>Zatvori</button>
               </div>}
            
            {popupPhase === "3" && <p>Događaj je dodan u Google kalendar.</p>}
            {popupPhase === "3" && calendarLink && <a href={calendarLink}>Vaš Google kalendar</a>}
            {popupPhase === "3" && 
               <div className="YesNoButtons">
                  <button onClick={() => window.location.reload()}>Zatvori</button>
               </div>}
         </div>
      </div>
   );
}