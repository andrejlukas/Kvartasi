import "../styles/login.css"
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import logo from "../assets/MojKvart.png"
import googleLogo from "../assets/google-logo.png"

export function Login() {
   const navigate = useNavigate();
   const [emailAddress, setEmailAddress] = useState('');
   const [password, setPassword] = useState('');

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
               throw new Error("Krivi podatci!");
            }
         })
         .then(data => {
               navigate('/home?token=' + data.token);
               window.location.reload();
               alert("Uspješna prijava!");
         })
         .catch( error => {
            navigate('/');
            alert("Kriva lozinka ili e-mail adresa!");
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
               <h2 className="naslov">Log in</h2>

               <label id="label" htmlFor="email">Email address</label>
               <input type="email" id="email" name="email"  className="inputs" onChange={(e) => setEmailAddress(e.target.value)}/>

               <div className="password-container">
               <label id="label" htmlFor="password">Your password</label>
               </div>
               <input type="password" id="password" name="password" className="inputs" onChange={(e) => setPassword(e.target.value)}/>

               <button type="submit">Log in</button>

               <div className="divider"></div>

               <p className="signup-text">Don't have an account?</p>
               <a href="/signup">
                  <button type="button">Sign up</button>
               </a>

               <a href="http://localhost:8080/oauth2/authorization/google" role="button" id="google-btn">
                  <img src={googleLogo} alt="Google Logo"/>
                  <span>Google prijava</span>
               </a>
            </form>
         </div>
      </div>
   );
}