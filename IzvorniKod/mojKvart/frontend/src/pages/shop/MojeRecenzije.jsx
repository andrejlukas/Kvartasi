import { useState, useEffect } from "react";
import { Navbar } from "../../components/ShopNavbar";

export function ShopMojeRecenzije() {
   const [email, setEmail] = useState("");
   const [trgovina, setTrgovina] = useState(null);
   const [recenzije, setRecenzije] = useState([]);
   const [popupRecenzija, setPopupRecenzija] = useState({
      recenzijaId: "",
      recenzijaOpis: "",
      recenzijaZvjezdice: "",
      recenzijaOdgovor: "",
      vrijemeKreiranja: "",
      odobrioModerator: "",
      kupacId: "",
      trgovinaId: ""
   });
   const [error, setError] = useState("");
   const [popupError, setPopupError] = useState("Vaš odgovor će biti poslan na odobravanje!");
       
   useEffect(() => {
      const token = localStorage.getItem("token");
      const options = {
         method: "POST",
         headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
         }, body: JSON.stringify({ oneLiner: token })
      };
       
      fetch("/api/tokens/claims", options)
         .then(async (response) => {
            if (!response.ok) {
               const text = await response.text();
               throw new Error(text);
            }
            return response.json();
         }).then((data) => {
            setEmail(data.email);
         }).catch((error) => setError(error.message));
   }, []);
   
   useEffect(() => {
      const token = localStorage.getItem("token");
      const options = {
         method: "GET",
         headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
         }
      };

      if (email) {
         fetch(`/api/trgovinas/${email}`, options)
            .then((response) => {
               if (!response.ok) {
                  throw new Error("Neuspješno dohvaćanje trgovine.");
               }
               return response.json();
            })
            .then((data) => {
               setTrgovina(data);
            })
            .catch((error) => {
               setError(error.message);
            });
      }
   }, [email]);
   
   useEffect(() => {
      const token = localStorage.getItem("token");
      const options = {
         method: "GET",
         headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
         }
      };

      if (trgovina) {
         fetch(`/api/recenzijas/trgovinas/${trgovina.trgovinaId}`, options)
            .then((response) => {
               if (!response.ok) {
                  throw new Error("Neuspješno dohvaćanje recenzija.");
               }
               return response.json();
            })
            .then((data) => {
               setRecenzije(data);
            })
            .catch((error) => {
               setError(error.message);
            });
      }
   }, [trgovina]);

   const handlePopupRecenzijaChange = (e) => {
      setPopupRecenzija({
         ...popupRecenzija,
         recenzijaOdgovor: e.target.value
      });
   };

   const openPopup = (recenzija) => {
      setPopupRecenzija(recenzija);
      document.getElementById("registrationPopup").style.display = "flex";
   };

   const closePopup = () => {
      setPopupError("Vaš odgovor će biti poslan na odobravanje!");
      setPopupRecenzija({
         recenzijaId: "",
         recenzijaOpis: "",
         recenzijaZvjezdice: "",
         recenzijaOdgovor: "",
         vrijemeKreiranja: "",
         odobrioModerator: true,
         kupacId: "",
         trgovinaId: ""
      });
      document.getElementById("registrationPopup").style.display = "none";
   };

   const sendResponse = () => {
      popupRecenzija.odobrioModerator = false;

      const token = localStorage.getItem("token");
      const options = {
         method: "PUT",
         headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json",
         }, body: JSON.stringify(popupRecenzija)
      }

      fetch(`/api/recenzijas/${popupRecenzija.recenzijaId}`, options)
         .then(async (response) => {
            if (!response.ok) {
               const text = await response.text();
               throw new Error(text);
            }
            return response.text();
         })
         .then(resp => {
            closePopup();
            window.location.reload();
         })
         .catch((error) => {
            setPopupError(error.message);
         });
   };
   
   return (
      <div>
         <Navbar/>
         <div id="vanjskic">
            <div id="listica">
               {error && <p style={{ color: "red" }}>{error}</p>}
               {recenzije.length > 0 ? (
                  <ul id="skupina">
                     {recenzije.map((recenzija) => (
                        <li id="tockica" key={recenzija.recenzijaId}>
                           <p id="rec">
                              <strong>{recenzija.recenzijaOpis}</strong>
                           </p>
                           <p>
                              <strong id="odg">Vaš odgovor:</strong> {recenzija.recenzijaOdgovor || "Nema odgovora"}
                           </p>
                           <p>
                              <button className="sendResponse" onClick={() => openPopup(recenzija)}>Uredi odgovor</button>
                           </p>
                        </li>
                     ))}
                  </ul>
               ) : (
                  <p id="dojava">Trenutno nema odobrenih recenzija.</p>
               )}
            </div>
         </div>
         <div id="registrationPopup">
            <p>Odgovorite na recenziju:</p>
            <strong>{popupRecenzija.recenzijaOpis}</strong>
            <input
               type="textarea"
               placeholder="Odgovor"
               className="dogadaj-inputs"
               name="popupRecenzija"
               value={popupRecenzija.recenzijaOdgovor || ""}
               onChange={handlePopupRecenzijaChange}
               maxLength={70}
            />
            {popupError && <p style={{"textAlign": "center", "color": "red"}}>{popupError}</p>}
            <div className="YesNoButtons">
               <button onClick={() => sendResponse()}>Odgovori</button>
               <button onClick={closePopup}>Odustani</button>
            </div>
         </div>
      </div>
   );
}