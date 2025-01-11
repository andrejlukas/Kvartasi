import { useState, useEffect } from "react";
import { Navbar } from "../../components/ShopNavbar";
import "../../styles/ShopDogadaji.css"

export function ShopDogadaji() {
   const [shopEmail, setShopEmail] = useState(null);
   const [shopId, setShopId] = useState(null);
   const [dogadajiType, setDogadajiType] = useState("upcoming");
   const [dogadaji, setDogadaji] = useState([]);
   const [error, setError] = useState("");
   const [dogadajData, setDogadajData] = useState({
      dogadajId: null,
      dogadajNaziv: "",
      dogadajOpis: "",
      dogadajVrijeme: "",
      dogadajSlika: "",
      trgovina: -1,
   });
   const [toUpdate, setToUpdate] = useState(false);

   useEffect(() => {
      const token = localStorage.getItem("token");
      const options = {
         method: "POST",
         headers: {
            "Content-Type": "application/json"
         },
         body: JSON.stringify({ oneLiner: token })
      };

      fetch("/api/tokens/claims", options)
         .then(async (response) => {
            if (!response.ok) {
               const text = await response.text();
               throw new Error(text);
            }
            return response.json();
         })
         .then((data) => {
            setShopEmail(data.email);
         })
         .catch((error) => setError(error.message));
   }, []);

   useEffect(() => {
      if (!shopEmail) return;

      const token = localStorage.getItem("token");
      const options = {
         method: "GET",
         headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
         },
      };

      fetch(`/api/trgovinas/${shopEmail}`, options)
         .then(async (response) => {
            if (!response.ok) {
               const text = await response.text();
               throw new Error(text);
            }
            return response.json();
         })
         .then((data) => {
            setShopId(data.trgovinaId);
         })
         .catch((error) => {
            setError(error.message);
         });
   }, [shopEmail]);

   useEffect(() => {
      if (!shopId) return;

      const token = localStorage.getItem("token");
      const options = {
         method: "GET",
         headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json",
         }
      };

      const endpoint =
         dogadajiType === "upcoming"
         ? `/api/dogadajs/upcoming/${shopId}`
         : `/api/dogadajs/finished/${shopId}`;

      fetch(endpoint, options)
         .then(async (response) => {
            if (!response.ok) {
               const text = await response.text();
               throw new Error(text);
            }
            return response.json();
         })
         .then((data) => {
            setDogadaji(data);
         })
         .catch((error) => {
            setError(error.message);
         });
   }, [shopId, dogadajiType]);

   const handleDogadajiTypeChange = (e) => {
      setDogadajiType(e.target.value);
   };

   const handleDogadajChange = (e) => {
      const { name, value } = e.target;
      setDogadajData((prevData) => ({ ...prevData, [name]: value }));
   };

   const showDogadajForm = () => {
      setToUpdate(false);
      const element = document.getElementById("vani2");
      element.style.cursor = "not-allowed";
      element.style.opacity = 0.5;
      document.getElementById("registrationPopup").style.display = "flex";
   };

   const closeDogadajForm = () => {
      const element = document.getElementById("vani2");
      element.style.cursor = "auto";
      element.style.opacity = 1;
      document.getElementById("registrationPopup").style.display = "none";
   };

   return (
      <div>
         <div id="vani2" style={{"minHeight": "100vh"}}>
            <Navbar />
            {error && <p>{error}</p>}
            <div id="events-alt" className="events-section-alt">
               <div id="eventController">
                  <label>
                     <input
                        type="radio"
                        name="dogadajiType"
                        value="upcoming"
                        checked={dogadajiType === "upcoming"}
                        onChange={handleDogadajiTypeChange}
                     />
                     Nadolazeći događaji
                  </label>
                  <label>
                     <input
                        type="radio"
                        name="dogadajiType"
                        value="finished"
                        checked={dogadajiType === "finished"}
                        onChange={handleDogadajiTypeChange}
                     />
                     Prijašnji događaji
                  </label>
                  <button id="addEvent" onClick={showDogadajForm}>Dodaj događaj</button>
               </div>
               <div className="row">
                  {dogadaji.length > 0 ? (
                     dogadaji.map((dogadaj) => (
                        <div id="eventBox" key={dogadaj.dogadajId} className="col-lg-4 col-md-6 col-12 mb-3-alt">
                           <div className="card-alt event-card-alt">
                              <img src={dogadaj.dogadajSlika} className="card-img-top-alt" alt={dogadaj.dogadajNaziv}/>
                              <div className="card-body-alt">
                                 <h5 className="card-title-alt">{dogadaj.dogadajNaziv}</h5>
                                 <p className="card-text-alt">{dogadaj.dogadajOpis}</p>
                                 <p className="date-alt">Vrijeme: {dogadaj.dogadajVrijeme}</p>
                              </div>
                           </div>
                        </div>
                     ))
                  ) : (<p>Nema događaja u ovoj kategoriji.</p>)}
               </div>
            </div>
         </div>
         <div id="registrationPopup" style={{ display: "none" }}>
            <input
               type="text"
               placeholder="Naziv događaja"
               className="dogadaj-inputs"
               name="dogadajNaziv"
               value={dogadajData.dogadajNaziv}
               onChange={handleDogadajChange}
            />
            <textarea
               placeholder="Opis događaja"
               className="dogadaj-inputs"
               name="dogadajOpis"
               value={dogadajData.dogadajOpis}
               onChange={handleDogadajChange}
            />
            <input
               type="text"
               placeholder="Vrijeme događaja (DD.MM.GGGG SS:mm)"
               className="dogadaj-inputs"
               name="dogadajVrijeme"
               value={dogadajData.dogadajVrijeme}
               onChange={handleDogadajChange}
            />
            <input
               type="text"
               placeholder="Slika događaja (URL)"
               className="dogadaj-inputs"
               name="dogadajSlika"
               value={dogadajData.dogadajSlika}
               onChange={handleDogadajChange}
            />
            <div className="YesNoButtons">
               <button onClick={() => {}}>Spremi događaj</button>
               <button onClick={closeDogadajForm}>Odustani</button>
            </div>
         </div>
      </div>
   );
}