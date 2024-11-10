import IMAGE from "../assets/vege.avif" //treba nam bolja slika 
import "../styles/login.css"
import { Link } from "react-router-dom";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import logo from "../assets/MojKvart.png"


export function Login() {
   const navigate = useNavigate();
   const [emailAddress, setEmailAddress] = useState('');
   const [password, setPassword] = useState('');
   const [emailExists, setEmailExists] = useState(false); // Dodato stanje za provjeru emaila
   const [logInTrigger, setTrigger] = useState(true); // Za obavijest o krivom pokušaju prijave

   useEffect(() => {
      if(!logInTrigger && !emailExists) 
         setTrigger(true);
      if (emailAddress.length > 0) {
         fetch('/api/kupacs')
            .then((response) => response.json())
            .then((registriraniKorisnici) => {
               const duplikat = registriraniKorisnici.some(
                  (korisnik) => korisnik.kupacEmail === emailAddress );
               setEmailExists(duplikat);
            });
      }
   }, [emailAddress]);

   function checkCorrectPassword(e){
      
      e.preventDefault();

      if (!emailExists) {
         setTrigger(false);
         return;
      }

      // ovo inicijaliziras prije options: const token = localStorage.getItem('token');
      // ovo dodajes u headers zahtjevau options: 'Authorization': `Bearer ${token}`,
      // ovdje to jasno ne treba jer tek ides u login

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
  
      /*return fetch('/api/kupacs/login', options) 
         .then(response => {
            if (response.ok) {
               localStorage.setItem('token', data.token);
               localStorage.setItem('role', data.role);
               navigate("/home");
               alert("Uspješna prijava!");
            } else {
               navigate('/');
            }
      });*/
      return fetch('/api/kupacs/login', options) 
         .then(response => {
            if (response.ok) {
               return response.json();
            } else {
               throw new Error("Krivi podatci!");
            }
         })
         .then(data => {
               localStorage.setItem('token', data.token);
               localStorage.setItem('role', data.role);
               navigate("/home");
               alert("Uspješna prijava!");
         })
         .catch( error => navigate('/') );

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
               {/* <span className="show-hide">Hide</span> */}
               </div>
               <input type="password" id="password" name="password" className="inputs" onChange={(e) => setPassword(e.target.value)}/>

               {!logInTrigger && (
                        <p style={{ color: "red", marginTop: "4px" ,  marginBottom: "4px", fontWeight: "normal" }}>
                           Wrong username or password!
                        </p>
               )}

               <button type="submit">Log in</button>

               <div className="divider"></div>

               <p className="signup-text">Don't have an account?</p>
               <Link to="/signup">
                  <button type="button">Sign up</button>
               </Link>
            </form>
         </div>
      </div>
   );
}