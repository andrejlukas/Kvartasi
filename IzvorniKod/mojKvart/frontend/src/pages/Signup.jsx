import "../styles/Signup.css";
import "leaflet/dist/leaflet.css";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { MapContainer, TileLayer, Marker, useMapEvents } from "react-leaflet";
import googleLogo from "../assets/google-logo.png";

export function Signup() {
   const navigate = useNavigate();
   const [registrationType, setRegistrationType] = useState("customer");
   const [customerData, setCustomerData] = useState({
      kupacIme: "",
      kupacPrezime: "",
      kupacAdresa: "",
      kupacEmail: "",
      kupacSifra: "",
      kupacStatus: "N"
   });
   const [shopData, setShopData] = useState({
      trgovinaNaziv: "",
      trgovinaOpis: "",
      trgovinaKategorija: "",
      trgovinaLokacija: "",
      trgovinaSlika: "",
      trgovinaRadnoVrijemeOd: "08:00",
      trgovinaRadnoVrijemeDo: "20:00",
      trgovinaEmail: "",
      trgovinaSifra: "",
      trgovinaStatus: "N"
   });
   const [moderatorData, setModeratorData] = useState({
      moderatorIme: "",
      moderatorPrezime: "",
      moderatorEmail: "",
      moderatorSifra: "",
      moderatorStatus: "N"
   });
   const [showPassword, setShowPassword] = useState(false);
   const [verificationCode, setVerificationCode] = useState("");
   const [errorMessage, setErrorMessage] = useState("");
   const [markerPosition, setMarkerPosition] = useState(null);

   async function getVerificationCode(e){   
      e.preventDefault();

      const options = {
         method: 'POST',
         headers: {
            'Content-Type': 'application/json'
         },
         body: JSON.stringify(customerData)
      };

      return fetch('/api/kupacs/signup', options)
         .then(async (response) => {
            if (response.ok) {
               setErrorMessage('');
               
               fetch('/api/kupacs/sendVerificationMail', {
                  method: 'POST',
                  headers: { 'Content-Type': 'application/json' },
                  body: JSON.stringify({ "oneLiner": customerData.kupacEmail })
               });

               localStorage.setItem("verificationData", JSON.stringify({ "email": customerData.kupacEmail, "verificationCode": "", "tries": 0 }));
               window.location.reload();
            } else {
               return response.text().then(text => {
                  setErrorMessage(text);
                  throw new Error('Request failed');
               });
            }
         })
         .catch( error => navigate('/signup') );
   }

   async function checkVerificationCode(e) {
      e.preventDefault();
   
      let data = JSON.parse(localStorage.getItem("verificationData"));
      data.verificationCode = verificationCode;
      data.tries = data.tries + 1;
   
      const options = {
         method: 'POST',
         headers: {
            'Content-Type': 'application/json'
         },
         body: JSON.stringify(data)
      };
   
      return fetch('/api/kupacs/verification', options)
         .then(async (response) => {
            if (!response.ok) {
               const text = await response.text();
               localStorage.setItem("verificationData", JSON.stringify(data));
               throw new Error(text);
            }
            return response.json();
         })
         .then((data) => {
            localStorage.removeItem("verificationData");
            navigate('/home?token=' + data.token);
            window.location.reload();
         })
         .catch(error => {
            if(error.message.startsWith("Vrijeme")){
               alert("Nemate više pravo upisivati verifikacijski kod nakon 5 minuta. Pokušajte se registrirati opet te pažljivo upišite svoje podatke.");
               localStorage.removeItem("verificationData");
               setErrorMessage('');
            } else if(data.tries == 3) {
               alert("Nemate više pravo upisivati verifikacijski kod nakon 3 pokušaja. Pokušajte se registrirati opet te pažljivo upišite svoje podatke.");
               localStorage.removeItem("verificationData");
               setErrorMessage('');
            } else {
               setErrorMessage(`Netočan verifikacijski kod!<br/>Broj preostalih pokušaja verifikacije: ${ 3 - data.tries }`);
            }
         });
   }

   async function registerShop(e) {
      e.preventDefault();

      const options = {
         method: 'POST',
         headers: {
            'Content-Type': 'application/json'
         },
         body: JSON.stringify(shopData)
      };

      return fetch("/api/trgovinas/signup", options)
         .then(async (response) => {
            if (!response.ok) {
               const text = await response.text();
               throw new Error(text);
            }
            return response.json();
         })
         .then((data) => {
            //navigate('/trgovina/home?token=' + data.token);
            //window.location.reload();
            //ovo promjeniti u
            navigate("/notverified");
         })
         .catch(error => setErrorMessage(error.message))
   }

   function registerModerator(e) {
      e.preventDefault();

      const options = {
         method: 'POST',
         headers: {
            'Content-Type': 'application/json'
         },
         body: JSON.stringify(moderatorData)
      };

      return fetch("/api/moderators/signup", options)
         .then(async (response) => {
            if (!response.ok) {
               return response.text().then((text) => {
                  throw new Error(text);
               });
            }
            return response.json();
         })
         .then((data) => {
            //navigate('/moderator/home?token=' + data.token);
            //window.location.reload();
            //ovo promjeniti u
            navigate("/notverified");
         })
         .catch(error => setErrorMessage(error.message))
   }

   const backToSignUp = () => {
      localStorage.removeItem("verificationData");
      window.location.reload();
   }

   const togglePasswordVisibility = () => {
      setShowPassword((prevShowPassword) => !prevShowPassword);
   };

   const handleRegistrationTypeChange = (e) => {
      setRegistrationType(e.target.value);
   };

   const handleCustomerChange = (e) => {
      const { name, value } = e.target;
      setCustomerData((prevData) => ({ ...prevData, [name]: value }));
   };

   const handleShopChange = (e) => {
      const { name, value } = e.target;
      setShopData((prevData) => ({ ...prevData, [name]: value }));
   };

   const handleModeratorChange = (e) => {
      const { name, value } = e.target;
      setModeratorData((prevData) => ({ ...prevData, [name]: value }));
   };

   const LocationMarker = () => {
      useMapEvents({
         click(e) {
            const { lat, lng } = e.latlng;
            setMarkerPosition([lat, lng]);
            if (registrationType === "customer") {
               setCustomerData((prevData) => ({
                  ...prevData,
                  kupacAdresa: `${lat},${lng}`,
               }));
            } else {
               setShopData((prevData) => ({
                  ...prevData,
                  trgovinaLokacija: `${lat},${lng}`,
               }));
            }
         },
      });

      return markerPosition ? <Marker position={markerPosition} /> : null;
   };

   const renderCustomerForm = () => (
      <>
         <div className="form-group">
            <input
               type="text"
               placeholder="Ime"
               className="signup-inputs"
               name="kupacIme"
               value={customerData.kupacIme}
               onChange={handleCustomerChange}
            />
            <input
               type="text"
               placeholder="Prezime"
               className="signup-inputs"
               name="kupacPrezime"
               value={customerData.kupacPrezime}
               onChange={handleCustomerChange}
            />
         </div>
         <div id="leafletMapCustomer">
            <label>Kućna adresa</label>
            <MapContainer
               center={markerPosition || [45.815, 15.9819]}
               zoom={12}
               style={{ height: "400px", width: "100%" }}
               key={markerPosition}
            >
               <TileLayer
                  url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                  attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
               />
               <LocationMarker />
            </MapContainer>
         </div>
         <input
            type="email"
            placeholder="E-mail adresa"
            className="signup-inputs"
            name="kupacEmail"
            value={customerData.kupacEmail}
            onChange={handleCustomerChange}
         />
         <div className="password-input-container">
            <input
               type={showPassword ? "text" : "password"}
               placeholder="Lozinka"
               name="kupacSifra"
               className="inputs"
               value={customerData.kupacSifra}
               onChange={handleCustomerChange}
            />
            <button
               type="button"
               onClick={togglePasswordVisibility}
               className="toggle-password-button-signup"
            >
               {showPassword ? "Sakrij" : "Otkrij"}
            </button>
         </div>
      </>
   );

   const renderShopForm = () => (
      <>
         <input
            type="text"
            placeholder="Naziv trgovine"
            className="signup-inputs"
            name="trgovinaNaziv"
            value={shopData.trgovinaNaziv}
            onChange={handleShopChange}
         />
         <textarea
            placeholder="Opis trgovine"
            className="signup-inputs"
            name="trgovinaOpis"
            value={shopData.trgovinaOpis}
            onChange={handleShopChange}
         />
         <input
            type="text"
            placeholder="Kategorija trgovine"
            className="signup-inputs"
            name="trgovinaKategorija"
            value={shopData.trgovinaKategorija}
            onChange={handleShopChange}
         />
         <label>Lokacija trgovine</label>
         <div id="leafletMapShop">
            <MapContainer
               center={markerPosition || [45.815, 15.9819]}
               zoom={12}
               style={{ height: "400px", width: "100%" }}
               key={markerPosition}
            >
               <TileLayer
                  url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                  attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
               />
               <LocationMarker />
            </MapContainer>
         </div>
         <input
            type="text"
            placeholder="Slika trgovine (URL)"
            className="signup-inputs"
            name="trgovinaSlika"
            value={shopData.trgovinaSlika}
            onChange={handleShopChange}
         />
         <div className="form-group">
            <input
               type="time"
               placeholder="Radno vrijeme od"
               className="signup-inputs"
               name="trgovinaRadnoVrijemeOd"
               value={shopData.trgovinaRadnoVrijemeOd}
               onChange={handleShopChange}
            />
            <input
               type="time"
               placeholder="Radno vrijeme do"
               className="signup-inputs"
               name="trgovinaRadnoVrijemeDo"
               value={shopData.trgovinaRadnoVrijemeDo}
               onChange={handleShopChange}
            />
         </div>
         <input
            type="email"
            placeholder="E-mail trgovine"
            className="signup-inputs"
            name="trgovinaEmail"
            value={shopData.trgovinaEmail}
            onChange={handleShopChange}
         />
         <div className="password-input-container">
            <input
               type={showPassword ? "text" : "password"}
               placeholder="Lozinka"
               name="trgovinaSifra"
               className="inputs"
               value={shopData.trgovinaSifra}
               onChange={handleShopChange}
            />
            <button
               type="button"
               onClick={togglePasswordVisibility}
               className="toggle-password-button-signup"
            >
               {showPassword ? "Sakrij" : "Otkrij"}
            </button>
         </div>
      </>
   );

   const renderModeratorForm = () => (
      <>
         <div className="form-group">
            <input
               type="text"
               placeholder="Ime"
               className="signup-inputs"
               name="moderatorIme"
               value={moderatorData.moderatorIme}
               onChange={handleModeratorChange}
            />
            <input
               type="text"
               placeholder="Prezime"
               className="signup-inputs"
               name="moderatorPrezime"
               value={moderatorData.moderatorPrezime}
               onChange={handleModeratorChange}
            />
         </div>
         <input
            type="email"
            placeholder="E-mail adresa"
            className="signup-inputs"
            name="moderatorEmail"
            value={moderatorData.moderatorEmail}
            onChange={handleModeratorChange}
         />
         <div className="password-input-container">
            <input
               type={showPassword ? "text" : "password"}
               placeholder="Lozinka"
               name="moderatorSifra"
               className="inputs"
               value={moderatorData.moderatorSifra}
               onChange={handleModeratorChange}
            />
            <button
               type="button"
               onClick={togglePasswordVisibility}
               className="toggle-password-button-signup"
            >
               {showPassword ? "Sakrij" : "Otkrij"}
            </button>
         </div>
      </>
   );


   if(localStorage.getItem("verificationData") != null) {
      return (
         <div id="verificationDiv">
            <h3>Verifikacijski kod je poslan na email adresu {JSON.parse(localStorage.getItem("verificationData")).email}</h3>
            <h2>Imate pet minuta i/ili 3 pokušaja da unesete kod nakon čega verifikacija više neće biti moguća.</h2>
            <form onSubmit={checkVerificationCode}>
               <input type="text" placeholder="Verifikacijski kod" name="verificationCode" value={verificationCode} 
               onChange={(e) => setVerificationCode(e.target.value)}/>
               <button type="submit">Verificiraj</button>
            </form>
            <button type="click" onClick={backToSignUp}>Natrag na registraciju</button>
            {errorMessage && <p className="error-message" dangerouslySetInnerHTML={{ __html: errorMessage }}/>}
         </div>
      );
   } else 
      return (
         <div className="signup-container">
            <div className="container">
               <div className="form-box">
                  <h2>Registracija</h2>
                  <div className="registration-type">
                     <label>
                        <input
                           type="radio"
                           name="registrationType"
                           value="customer"
                           checked={registrationType === "customer"}
                           onChange={handleRegistrationTypeChange}
                        />
                        Registracija kupca
                     </label>
                     <label>
                        <input
                           type="radio"
                           name="registrationType"
                           value="shop"
                           checked={registrationType === "shop"}
                           onChange={handleRegistrationTypeChange}
                        />
                        Registracija trgovine
                     </label>
                     <label>
                        <input
                           type="radio"
                           name="registrationType"
                           value="moderator"
                           checked={registrationType === "moderator"}
                           onChange={handleRegistrationTypeChange}
                        />
                        Registracija moderatora
                     </label>
                  </div>
                  <form onSubmit={(e) => registrationType === "customer" ? getVerificationCode(e) : 
                                         (registrationType === "shop" ? registerShop(e) : registerModerator(e))}>
                     {registrationType === "customer"
                        ? renderCustomerForm()
                        : (registrationType === "shop" ? renderShopForm() : renderModeratorForm())}
                     <button type="submit" className="signup-buttons">
                        {registrationType === "customer"
                           ? "Registriraj se"
                           : (registrationType === "shop" ? "Registriraj trgovinu" : "Registriraj se kao moderator")}
                     </button>
                     {registrationType === "customer" && <a href={`${import.meta.env.VITE_BACKEND_URL}/oauth2/authorization/google`} role="button" id="google-btn">
                        <img src={googleLogo} alt="Google Logo"/>
                        <span>Google registracija</span>
                     </a>}
                     {errorMessage && <p className="error-message">{errorMessage}</p>}
                     <a href="/">
                        <button id="Back" type="button" className="signup-buttons">
                           Natrag na prijavu
                        </button>
                     </a>
                  </form>
               </div>
            </div>
         </div>
      );
}