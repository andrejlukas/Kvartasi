import "../styles/Signup.css"
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import googleLogo from "../assets/google-logo.png"

export function Signup() {
   const navigate = useNavigate();
   const [firstName, setFirstName] = useState('')
   const [lastName, setLastName] = useState('')
   const [homeAddress, setHomeAddress] = useState('')
   const [emailAddress, setEmailAddress] = useState('')
   const [password, setPassword] = useState('')

   const [showPassword, setShowPassword] = useState(false);
   const [verificationCode, setVerificationCode] = useState('');
   const [errorMessage, setErrorMessage] = useState('');

   const togglePasswordVisibility = () => {
      setShowPassword((prevShowPassword) => !prevShowPassword);
   };


   function getVerificationCode(e){   
      e.preventDefault();

      const data = {
         kupacEmail: emailAddress,
         kupacIme: firstName,
         kupacPrezime: lastName,
         kupacAdresa: homeAddress,
         kupacSifra: password,
         verifikacijskiKod: null,
         kodValidanDo: null,
         verificiranKupac: false
      };
      const options = {
         method: 'POST',
         headers: {
            'Content-Type': 'application/json'
         },
         body: JSON.stringify(data)
      };

      return fetch('/api/kupacs/verification', options)
         .then(response => {
            if (response.ok) {
               setErrorMessage('');
               const verificationData = { "data": data, "tries": 0 };
               localStorage.setItem("verificationData", JSON.stringify(verificationData));
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

   function checkVerificationCode(e) {
      e.preventDefault();
   
      let data = JSON.parse(localStorage.getItem("verificationData")).data;
      let tries  = JSON.parse(localStorage.getItem("verificationData")).tries;

      data.verifikacijskiKod = verificationCode;
   
      const options = {
         method: 'POST',
         headers: {
            'Content-Type': 'application/json'
         },
         body: JSON.stringify(data)
      };
   
      return fetch('/api/kupacs/signup', options)
         .then((response) => {
            if (!response.ok) {
               return response.text().then((text) => {
                  localStorage.setItem("verificationData", JSON.stringify({ "data": data, "tries": tries + 1 }));
                  throw new Error(text);
               });
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
            } else if(tries == 2) {
               alert("Nemate više pravo upisivati verifikacijski kod nakon 3 pokušaja. Pokušajte se registrirati opet te pažljivo upišite svoje podatke.");
               localStorage.removeItem("verificationData");
               setErrorMessage('');
            } else {
               setErrorMessage(`Netočan verifikacijski kod!<br/>Broj preostalih pokušaja verifikacije: ${ 2 - tries }`);
            }
         });
   }
   
   
   if(localStorage.getItem("verificationData") != null) {
      return (
         <div>
            <h3>Verifikacijski kod je poslan na email adresu {JSON.parse(localStorage.getItem("verificationData")).data.kupacEmail}</h3>
            <h2>Imate pet minuta i/ili 3 pokušaja da unesete kod nakon čega verifikacija više neće biti moguća.</h2>
            <form onSubmit={checkVerificationCode}>
               <input type="text" placeholder="Verifikacijski kod" name="verificationCode" value={verificationCode} 
               onChange={(e) => setVerificationCode(e.target.value)}/>
               <button type="submit">Verificiraj</button>
            </form>
            {errorMessage && <p className="error-message" dangerouslySetInnerHTML={{ __html: errorMessage }}/>}
         </div>
      );
   } else 
      return (
         <div className="signup-container">
            <div className="container">
               <div className="form-box">
                  <h2>Registracija</h2>
                  <form onSubmit={getVerificationCode}>
                     <div className="form-group">
                        <input type="text" placeholder="Ime" className="signup-inputs" name="firstName" value={firstName}
                        onChange={(e) => setFirstName(e.target.value)}/>
                        <input type="text" placeholder="Prezime" className="signup-inputs" name="lastName" value={lastName}
                        onChange={(e) => setLastName(e.target.value)}/>
                     </div>
                     <input type="text" placeholder="Kućna adresa" id="home" className="signup-inputs"  name="homeAddress" value={homeAddress}
                        onChange={(e) => setHomeAddress(e.target.value)}/>
                     <input type="email" placeholder="E-mail adresa" id = "email" className="signup-inputs"  name="emailAddress" value={emailAddress}
                        onChange={(e) => setEmailAddress(e.target.value)}/>
                        
                     
                     <div className="password-input-container">
                     <input type={showPassword ? "text" : "password"} placeholder="Lozinka" id="password" name="password" className="inputs" onChange={(e) => setPassword(e.target.value)}/>
                     <button
                              type="button"
                              onClick={togglePasswordVisibility}
                              className="toggle-password-button-signup"
                           >
                              {showPassword ? "Sakrij" : "Otkrij"}
                           </button>
                     </div>
                     <button type="submit" className="signup-buttons" >Registriraj se</button>
                     {errorMessage && <p className="error-message">{errorMessage}</p>}
                     <a href="/">
                        <button id="Back" type="button" className="signup-buttons">Natrag na prijavu</button>
                     </a>
                     <a href={`${import.meta.env.VITE_BACKEND_URL}/oauth2/authorization/google`} role="button" id="google-btn">
                        <img src={googleLogo} alt="Google Logo"/>
                        <span>Google registracija</span>
                     </a>
                  </form>
               
               </div>
            </div>
         </div>
      );
}