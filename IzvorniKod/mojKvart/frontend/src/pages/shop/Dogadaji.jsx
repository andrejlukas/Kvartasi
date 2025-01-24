import { useState, useEffect } from "react";
import { Navbar } from "../../components/ShopNavbar";
import "../../styles/ShopDogadaji.css"

export function ShopDogadaji() {
   const [shopEmail, setShopEmail] = useState(null);
   const [shopId, setShopId] = useState(null);
   const [dogadajiType, setDogadajiType] = useState("upcoming");
   const [dogadaji, setDogadaji] = useState([]);
   const [error, setError] = useState("");
   const [popupError, setPopupError] = useState("");
   const [dogadajData, setDogadajData] = useState({
      dogadajId: "placeholder",
      dogadajNaziv: "",
      dogadajOpis: "",
      dogadajPocetak: "",
      dogadajKraj: "",
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

   const showDogadajForm = (bool) => {
      setToUpdate(bool);
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
      setPopupError("");
      setDogadajData({
         dogadajId: "placeholder",
         dogadajNaziv: "",
         dogadajOpis: "",
         dogadajPocetak: "",
         dogadajKraj: "",
         dogadajSlika: "",
         trgovina: -1,
      });
   };

   const createDogadaj = () => {
      dogadajData.dogadajId = null;
      dogadajData.trgovina = shopId;
      const token = localStorage.getItem("token");
      const options = {
         method: "POST",
         headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json",
         }, body: JSON.stringify(dogadajData)
      }

      fetch("/api/dogadajs", options)
         .then(async (response) => {
            if (!response.ok) {
               const text = await response.text();
               throw new Error(text);
            }
            return response.text();
         })
         .then(resp => {
            closeDogadajForm();
            window.location.reload();
         })
         .catch((error) => {
            setPopupError(error.message);
         });
   };

   const updateDogadaj = () => {
      const token = localStorage.getItem("token");
      const options = {
         method: "PUT",
         headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json",
         }, body: JSON.stringify(dogadajData)
      }

      fetch(`/api/dogadajs/${dogadajData.dogadajId}`, options)
         .then(async (response) => {
            if (!response.ok) {
               const text = await response.text();
               throw new Error(text);
            }
            return response.text();
         })
         .then(resp => {
            closeDogadajForm();
            window.location.reload();
         })
         .catch((error) => {
            setPopupError(error.message);
         });
   };

   const deleteDogadaj = (dogadajId) => {
      const token = localStorage.getItem("token");
      const options = {
         method: "DELETE",
         headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
         }
      }

      fetch(`/api/dogadajs/${dogadajId}`, options)
         .then(async (response) => {
            if (!response.ok) {
               const text = await response.text();
               throw new Error(text);
            }
            return response.text();
         })
         .then(resp => { window.location.reload(); })
         .catch((error) => {
            setPopupError(error.message);
         });
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
                  <button id="addEvent" onClick={() => showDogadajForm(false)}>Dodaj događaj</button>
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
                                 <p className="date-alt">Datum i vrijeme: {dogadaj.dogadajPocetak + " - " + dogadaj.dogadajKraj}</p>
                                 <div id="bottomPairDogadaj">
                                    <button className="add-to-cart-btn-alt" onClick={() => {
                                       setDogadajData(dogadaj);
                                       showDogadajForm(true);
                                    }}>Ažuriraj događaj</button>
                                    <button className="add-to-cart-btn-alt" onClick={() => {deleteDogadaj(dogadaj.dogadajId);}}>
                                       Izbriši događaj
                                    </button>
                                 </div>
                              </div>
                           </div>
                        </div>
                     ))
                  ) : (<p>Nema događaja u ovoj kategoriji.</p>)}
               </div>
            </div>
         </div>
         <div id="registrationPopup">
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
               placeholder="Vrijeme početka događaja (DD.MM.GGGG SS:mm)"
               className="dogadaj-inputs"
               name="dogadajPocetak"
               value={dogadajData.dogadajPocetak}
               onChange={handleDogadajChange}
            />
            <input
               type="text"
               placeholder="Vrijeme kraja događaja (DD.MM.GGGG SS:mm)"
               className="dogadaj-inputs"
               name="dogadajKraj"
               value={dogadajData.dogadajKraj}
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
            {popupError && <p style={{"textAlign": "center", "color": "red"}}>{popupError}</p>}
            <div className="YesNoButtons">
               <button onClick={() => toUpdate ? updateDogadaj() : createDogadaj()}>Spremi događaj</button>
               <button onClick={closeDogadajForm}>Odustani</button>
            </div>
         </div>
      </div>
   );
}