import { Navbar } from "../../components/ShopNavbar";
import { useState, useEffect } from "react";
import '../../styles/ShopPonudeiPromocije.css'

export function ShopPonudeiPromocije() {
   const [shopEmail, setShopEmail] = useState(null);
   const [shopId, setShopId] = useState(null);
   const [error, setError] = useState("");
   const [choiceType, setChoiceType] = useState("validPopust");
   const [dataToListout, setDataToListout] = useState([]);
   const [ponudaPopustDTO, setPonudaPopustDTO] = useState({
      ponudaPopustId: -1,
      ponudaPopustFlag: false,
      trgovina: -1
   });
   const [ponudaDTO, setPonudaDTO] = useState({
      ponudaId: -1,
      ponudaNaziv: "",
      ponudaOpis: "",
      ponudaRok: null,
      ponudaPopust: -1,
      trgovinaIme: ""
   });
   const [popustDTO, setPopustDTO] = useState({
      popustId: -1,
      popustQrkod: "",
      popustNaziv: "",
      popustOpis: "",
      popustRok: null,
      ponudaPopust: -1,
      trgovinaIme: ""
   });

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

      let endpoint;
      if(choiceType === "validPopust") endpoint = `/api/popusts/valid/${shopId}`;
      else if(choiceType === "invalidPopust") endpoint = `/api/popusts/invalid/${shopId}`;
      else if(choiceType === "validPonuda") endpoint = `/api/ponudas/valid/${shopId}`;
      else endpoint = `/api/ponudas/invalid/${shopId}`;

      fetch(endpoint, options)
         .then(async (response) => {
            if (!response.ok) {
               const text = await response.text();
               throw new Error(text);
            }
            return response.json();
         })
         .then((data) => {
            setDataToListout(data);
         })
         .catch((error) => {
            setError(error.message);
         });
   }, [shopId, choiceType]);

   const handleRadioChange = (event) => {
      setChoiceType(event.target.value);
   };
   
   return (
      <div>
         <Navbar/>
         <div id="eventController">
            <label>
               <input
                  type="radio"
                  value="validPopust"
                  checked={choiceType === "validPopust"}
                  onChange={handleRadioChange}
               />
               Odobreni važeći popusti
            </label>
            <label>
               <input
                  type="radio"
                  value="invalidPopust"
                  checked={choiceType === "invalidPopust"}
                  onChange={handleRadioChange}
               />
               Neodobreni ili istekli popusti
            </label>
            <label>
               <input
                  type="radio"
                  value="validPonuda"
                  checked={choiceType === "validPonuda"}
                  onChange={handleRadioChange}
               />
               Odobrene važeće ponude
            </label>
            <label>
               <input
                  type="radio"
                  value="invalidPonuda"
                  checked={choiceType === "invalidPonuda"}
                  onChange={handleRadioChange}
               />
               Neodobrene ili istekle ponude
            </label>
            <div id="addButtonsController">
               <button className="addPiPbutton">Dodaj popust</button>
               <button className="addPiPbutton">Dodaj ponudu</button>
            </div>
         </div>

         {dataToListout.length > 0 ?
            (<div id="PiPdataList">
               {choiceType === "validPopust" || choiceType === "invalidPopust" ? (
                  <div className="PiPbracket">
                     {dataToListout.map((popust) => (
                        <div className="bracketItem">
                           <p><strong>QR Kod:</strong> {popust.popustQrkod}</p>
                           <p><strong>Naziv:</strong> {popust.popustNaziv}</p>
                           <p><strong>Opis:</strong> {popust.popustOpis}</p>
                           <p><strong>Rok:</strong> {new Date(popust.popustRok).toLocaleDateString()}</p>
                        </div>
                     ))}
                  </div> ) : (
                  <div className="PiPbracket">
                     {dataToListout.map((ponuda) => (
                        <div className="bracketItem">
                           <p><strong>Naziv:</strong> {ponuda.ponudaNaziv}</p>
                           <p><strong>Opis:</strong> {ponuda.ponudaOpis}</p>
                           <p><strong>Rok:</strong> {new Date(ponuda.ponudaRok).toLocaleDateString()}</p>
                        </div>
                     ))}
                  </div>
               )}
            </div>) : (
               <p>Nemate takvih {choiceType === "validPopust" || choiceType === "invalidPopust" ? "popusta" : "ponuda"}.</p>
            )
         }
      </div>
   );
}