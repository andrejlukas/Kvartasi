import "../styles/login.css"
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import logo from "../assets/MojKvart.png"
import googleLogo from "../assets/google-logo.png"

export function Login() {
   const navigate = useNavigate();
   const [emailAddress, setEmailAddress] = useState('');
   const [password, setPassword] = useState('');
   const [errorMessage, setErrorMessage] = useState('');

   const [showPassword, setShowPassword] = useState(false);

   const togglePasswordVisibility = () => {
      setShowPassword((prevShowPassword) => !prevShowPassword);
   };

   function checkCorrectPassword(e){
      
      e.preventDefault();

      const data = {
        email: emailAddress,
        sifra : password
      };
      const options = {
         method: 'POST',
         headers: {
            'Content-Type': 'application/json'
         },
         body: JSON.stringify(data)
      };
  
      return fetch('/api/kupacs/login', options) 
         .then(response => {
            if (response.ok) {
               return response.json();
            } else {
               return response.text().then(text => {
                  setErrorMessage(text);
                  throw new Error('Request failed');
              });
            }
         })
         .then(data => {
            navigate('/home?token=' + data.token);
            window.location.reload();
         })
         .catch( error => {
            navigate('/');
         });
   }

   return (
      <div className="login-container">
         <div className="image-container">
            <div className="overlay">            
               <div className="overlay-box">
                  
                  <h1>
                  <img src={logo} alt="Logo" style={{ width: '200 px' }}></img>

                  </h1>
                  
                  <p id="kupujlokalno">Kupuj lokalno!</p>
               </div>
            </div>        

         
         </div>
         <div className="big-container ">
               
            <form className="login-form" onSubmit={ checkCorrectPassword }>
               <h2 className="naslov">Prijava</h2>

               <label id="label" htmlFor="email">E-mail adresa</label>
               <input type="email" id="email" name="email"  className="inputs" onChange={(e) => setEmailAddress(e.target.value)}/>

               <div className="password-container">
               <label id="label" htmlFor="password">Lozinka</label>
               </div>
               <div className="password-input-container">
               <input type={showPassword ? "text" : "password"} id="password" name="password" className="inputs" onChange={(e) => setPassword(e.target.value)}/>
               <button
                        type="button"
                        onClick={togglePasswordVisibility} // Dodaj funkciju za prikazivanje/sakrivanje
                        className="toggle-password-button"
                     >
                        {showPassword ? "Sakrij" : "Otkrij"}
                     </button>
               </div>
              

               <button type="submit">Prijavi se</button>

               {errorMessage && <p className="error-message">{errorMessage}</p>}

               <div className="divider"></div>

               <p className="signup-text">Niste još stvorili vaš korisnički račun?</p>
               <a href="/signup">
                  <button type="button">Kreiraj novi korisnički račun</button>
               </a>

               <a href={`${import.meta.env.VITE_BACKEND_URL}/oauth2/authorization/google`} role="button" id="google-btn">
                  <img src={googleLogo} alt="Google Logo"/>
                  <span>Google prijava</span>
               </a>
            </form>
         </div>
      </div>
   );
}