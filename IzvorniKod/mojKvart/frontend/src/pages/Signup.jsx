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
   const [emailExists, setEmailExists] = useState(false); // Dodato stanje za provjeru emaila

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
            }
         }).then(data => {
            navigate('/home?token=' + data.token);
            window.location.reload();
            alert("Uspješna registracija!");
         })
      .catch(error => {
         setEmailExists(true);
         console.error("Greška prilikom provjere e-maila:", error);
      });

       
   }

   
   return (
      <div className="signup-container">
         <div className="container">
            <div className="form-box">
               <h2>Sign up</h2>
               <form onSubmit={saveNoviClan}>
                  <div className="form-group">
                     <input type="text" placeholder="First name" className="signup-inputs" name="firstName" value={firstName}
                     onChange={(e) => setFirstName(e.target.value)}/>
                     <input type="text" placeholder="Last name" className="signup-inputs" name="lastName" value={lastName}
                     onChange={(e) => setLastName(e.target.value)}/>
                  </div>
                  <input type="text" placeholder="Home address" id="home" className="signup-inputs"  name="homeAddress" value={homeAddress}
                     onChange={(e) => setHomeAddress(e.target.value)}/>
                  <input type="email" placeholder="Email address" id = "email" className="signup-inputs"  name="emailAddress" value={emailAddress}
                     onChange={(e) => setEmailAddress(e.target.value)} required />
                      
                  
                  <div className="password-input-container">
                  <input type={showPassword ? "text" : "password"} id="password" name="password" className="inputs" onChange={(e) => setPassword(e.target.value)}/>
                  <button
                           type="button"
                           onClick={togglePasswordVisibility} // Dodaj funkciju za prikazivanje/sakrivanje
                           className="toggle-password-button-signup"
                        >
                           {showPassword ? "Hide" : "Show"}
                        </button>
                  </div>
                  <button type="submit" className="signup-buttons" >Sign up</button>
                  <a href="/">
                     <button id="Back" type="button" className="signup-buttons">Back to Sign in</button>
                  </a>
               <a href="http://localhost:8080/oauth2/authorization/google" role="button" id="google-btn">
                  <img src={googleLogo} alt="Google Logo"/>
                  <span>Google registracija</span>
               </a>
               </form>

               
            </div>
         </div>
      </div>
   
   );

}