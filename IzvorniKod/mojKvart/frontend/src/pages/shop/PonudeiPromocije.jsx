import { Navbar } from "../../components/ShopNavbar";
import { useState, useEffect } from "react";
import '../../styles/ShopPonudeiPromocije.css'

export function ShopPonudeiPromocije() {
   const [shopEmail, setShopEmail] = useState(null);
   const [shopId, setShopId] = useState(null);
   const [error, setError] = useState("");
   const [popupError, setPopupError] = useState("Popust će biti poslan na odobravanje!");
   const [choiceType, setChoiceType] = useState("validPopust");
   const [dataToListout, setDataToListout] = useState([]);
   const [popustToUpdate, setPopustToUpdate] = useState(false);
   const ponudaPopustDTO = {
      ponudaPopustId: null,
      ponudaPopustFlag: false,
      trgovina: -1
   }
   const [popustDTO, setPopustDTO] = useState({
      popustId: -1,
      popustQrkod: "",
      popustNaziv: "",
      popustOpis: "",
      popustRok: "",
      ponudaPopust: -1,
      trgovinaIme: ""
   });
   const [ponudaDTO, setPonudaDTO] = useState({
      ponudaId: -1,
      ponudaNaziv: "",
      ponudaOpis: "",
      ponudaRok: "",
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
   
   const handlePopustChange = (e) => {
      const { name, value } = e.target;
      setPopustDTO((prevData) => ({ ...prevData, [name]: value }));
   };

   const closePopustForm = () => {
      setPopustDTO({
         popustId: -1,
         popustQrkod: "",
         popustNaziv: "",
         popustOpis: "",
         popustRok: "",
         ponudaPopust: -1
      });
      document.getElementById("registrationPopup").style.display = "none";
      setPopupError("Popust će biti poslan na odobravanje!");
      setPopustToUpdate(false);
   };

   const setUpdatePopust = (popust) => {
      setPopustDTO(popust);
      setPopustToUpdate(true);
      document.getElementById("registrationPopup").style.display="flex";
   }; 

   const createPopust = () => {
      const token = localStorage.getItem("token");
      let options = {
         method: "POST",
         headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json",
         }, body: JSON.stringify(popustDTO)
      };

      fetch("/api/popusts/check", options)
         .then(async (response) => {
            if (!response.ok) {
               const text = await response.text();
               throw new Error(text);
            }
         }).then(resp => {
            ponudaPopustDTO.trgovina = shopId;
            options = {
               method: "POST",
               headers: {
                  "Authorization": `Bearer ${token}`,
                  "Content-Type": "application/json",
               }, body: JSON.stringify(ponudaPopustDTO)
            };
            fetch(`/api/ponudaPopusts`, options)
               .then(async (response) => {
                  if (!response.ok) {
                     const text = await response.text();
                     throw new Error(text);
                  }
                  return response.json();
               })
               .then((id) => {
                  popustDTO.popustId = null;
                  popustDTO.ponudaPopust = id;
                  options = {
                     method: "POST",
                     headers: {
                        "Authorization": `Bearer ${token}`,
                        "Content-Type": "application/json",
                     }, body: JSON.stringify(popustDTO)
                  };
                  fetch(`/api/popusts`, options)
                     .then(async (response) => {
                        if (!response.ok) {
                           const text = await response.text();
                           throw new Error(text);
                        }
                        return response.json();
                     })
                     .then((id) => window.location.reload())
                     .catch(error => setError(error.message));
               }).catch(error => setError(error.message));
         }).catch(error => setPopupError(error.message))
   };

   const updatePopust = () => {
      const token = localStorage.getItem("token");
      let options = {
         method: "PUT",
         headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json",
         }, body: JSON.stringify(popustDTO)
      };

      fetch(`/api/popusts/${popustDTO.popustId}`, options)
         .then(async (response) => {
            if (!response.ok) {
               const text = await response.text();
               throw new Error(text);
            }
            return response.json();
         })
         .then((data) => {
            ponudaPopustDTO.ponudaPopustId = popustDTO.ponudaPopust;
            ponudaPopustDTO.trgovina = shopId;
            options = {
               method: "PUT",
               headers: {
                  "Authorization": `Bearer ${token}`,
                  "Content-Type": "application/json",
               }, body: JSON.stringify(ponudaPopustDTO)
            };
            fetch(`/api/ponudaPopusts/${popustDTO.ponudaPopust}`, options)
               .then(async (response) => {
                  if (!response.ok) {
                     const text = await response.text();
                     throw new Error(text);
                  }
                  return response.json();
               }).then(resp => window.location.reload())
               .catch(e => setError(e.message));
         })
         .catch((error) => {
            setPopupError(error.message);
         });
   };

   const deletePopust = (popust) => {
      const token = localStorage.getItem("token");
      const options = {
         method: "DELETE",
         headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json",
         }
      };
      
      fetch(`/api/popusts/${popust.popustId}`, options)
         .then(async (response) => {
            if (!response.ok) {
               const text = await response.text();
               throw new Error(text);
            }
            return response.text();
         }).then(resp => {
            fetch(`/api/ponudaPopusts/${popust.ponudaPopust}`, options)
               .then(async (response) => {
                  if (!response.ok) {
                     const text = await response.text();
                     throw new Error(text);
                  }
                  return response.text();
               }).then(resp => window.location.reload())
         }).catch(e => setError("Neuspjel pokušaj brisanja popusta."))
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
               <button className="addPiPbutton" onClick={() => document.getElementById("registrationPopup").style.display="flex"}>Dodaj popust</button>
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
                           <p><strong>Rok:</strong> {popust.popustRok}</p>
                           <div className="YesNoButtons">
                              <button onClick={() => setUpdatePopust(popust)}>Ažuriraj</button>
                              <button onClick={() => deletePopust(popust)}>Obriši</button>
                           </div>
                        </div>
                     ))}
                  </div> ) : (
                  <div className="PiPbracket">
                     {dataToListout.map((ponuda) => (
                        <div className="bracketItem">
                           <p><strong>Naziv:</strong> {ponuda.ponudaNaziv}</p>
                           <p><strong>Opis:</strong> {ponuda.ponudaOpis}</p>
                           <p><strong>Rok:</strong> {ponuda.ponudaRok}</p>
                        </div>
                     ))}
                  </div>
               )}
            </div>) : (
               <p>Nemate takvih {choiceType === "validPopust" || choiceType === "invalidPopust" ? "popusta" : "ponuda"}.</p>
            )
         }

         <div id="registrationPopup">
            <input
               type="text"
               placeholder="Naziv popusta"
               className="proizvod-inputs"
               name="popustNaziv"
               value={popustDTO.popustNaziv}
               onChange={handlePopustChange}
               maxLength={50}
            />
            <textarea
               type="text"
               placeholder="Opis popusta"
               className="proizvod-inputs"
               name="popustOpis"
               value={popustDTO.popustOpis}
               onChange={handlePopustChange}
               maxLength={200}
            />
            <input
               type="text"
               placeholder="QR kod popusta"
               className="proizvod-inputs"
               name="popustQrkod"
               value={popustDTO.popustQrkod}
               onChange={handlePopustChange}
               maxLength={50}
            />
            <input
               type="text"
               placeholder="Rok trajanja popusta u formatu DD.MM.GGGG SS:mm"
               className="proizvod-inputs"
               name="popustRok"
               value={popustDTO.popustRok}
               onChange={handlePopustChange}
               maxLength={50}
            />
            {popupError && <p style={{"textAlign": "center", "color": "red"}}>{popupError}</p>}
            {!popustToUpdate && <div className="YesNoButtons">
               <button onClick={createPopust}>Spremi popust</button>
               <button onClick={closePopustForm}>Odustani</button>
            </div>}
            {popustToUpdate && <div className="YesNoButtons">
               <button onClick={updatePopust}>Ažuriraj popust</button>
               <button onClick={closePopustForm}>Odustani</button>
            </div>}
         </div>
      </div>
   );
}