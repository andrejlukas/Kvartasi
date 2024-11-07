import IMAGE from "../assets/vege.avif" //treba nam bolja slika 
import "../styles/login.css"
import { Link } from "react-router-dom";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

export function Login() {
   const navigate = useNavigate();
   const firstName = "";
   const lastName = "";
   const homeAddress = "";
   const [emailAddress, setEmailAddress] = useState('');
   const [password, setPassword] = useState('');
   const [emailExists, setEmailExists] = useState(false); // Dodato stanje za provjeru emaila
   const [logInTrigger, setTrigger] = useState(true);

   useEffect(() => {
      if (emailAddress.length > 0) {
         fetch(`/api/kupacs`)
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
         console.log("Ova email adresa ne postoji!");
         return;
      }

      const data = {
        kupacEmail: emailAddress,
        kupacIme: firstName,
        kupacPrezime: lastName,
        kupacAdresa : homeAddress,
        kupacSifra : password
      };
      const options = {
         method: 'POST',
         headers: {
            'Content-Type': 'application/json'
         },
         body: JSON.stringify(data)
      };
      console.log(JSON.stringify(data))
  
      return fetch('/api/kupacs', options) // nesto ovdje popraviti, baca CORS ERROR
         .then(response => {
            if (response.ok) {
               navigate("/home");
               alert("Uspješna prijava!");
               console.log("sve okej, idemo na home page");
            } else {
               navigate('/');
               console.log("pa zasto smo ovdje")
            }
         });
   }

   return (
      <div className="login-container">
         <div className="image-container">
            <div className="overlay">            
               <div className="overlay-box">
                  
                  <h1>
                  <span style={{ fontWeight: "bold", color: "black" }}>Moj</span>
                  <span style={{ fontWeight: "bold", color: "#098443" }}>Kvart</span>
                  </h1>
                  
                  <p>Kupuj lokalno!</p>
               </div>
            </div>        
         <img src={IMAGE} className="responsive-image" />
         
         </div>
         <div className="big-container ">
               
            <form className="login-form" onSubmit={ checkCorrectPassword }>
               <h2 className="naslov">Log in</h2>

               <label htmlFor="email">Email address</label>
               <input type="email" id="email" name="email"  className="inputs" onChange={(e) => setEmailAddress(e.target.value)}/>

               <div className="password-container">
                  <label htmlFor="password">Your password</label>
               {/* <span className="show-hide">Hide</span> */}
               </div>
               <input type="password" id="password" name="password" className="inputs" onChange={(e) => setPassword(e.target.value)}/>

               {!logInTrigger && (
                        <p style={{ color: "red", marginTop: "4px" ,  marginBottom: "4px", fontWeight: "bold" }}>
                           This e-mail address doesn't exist! Sign up?
                        </p>
               )}

               <button type="submit" disabled={ !emailExists }>Log in</button>

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