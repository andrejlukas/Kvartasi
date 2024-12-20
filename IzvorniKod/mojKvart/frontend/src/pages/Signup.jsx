import "../styles/signup.css"
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

   const [errorMessage, setErrorMessage] = useState('');

   const [showPassword, setShowPassword] = useState(false);

   const togglePasswordVisibility = () => {
      setShowPassword((prevShowPassword) => !prevShowPassword);
   };


   function saveNoviClan(e){
      
      e.preventDefault();
      
      const data = {
         kupacEmail: emailAddress,
         kupacIme: firstName,
         kupacPrezime: lastName,
         kupacAdresa: homeAddress,
         kupacSifra: password
      };
      const options = {
         method: 'POST',
         headers: {
            'Content-Type': 'application/json'
         },
         body: JSON.stringify(data)
      };

      return fetch('/api/kupacs/signup', options)
         .then(response => {
            if (response.ok) {
               return response.json();
            } else {
               return response.text().then(text => {
                  setErrorMessage(text);
                  throw new Error('Request failed');
              });
            }
         }).then(data => {
            navigate('/home?token=' + data.token);
            window.location.reload();
         })
         .catch( error => navigate('/signup') );
   }

   
   return (
      <div className="signup-container">
         <div className="container">
            <div className="form-box">
               <h2>Registracija</h2>
               <form onSubmit={saveNoviClan}>
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