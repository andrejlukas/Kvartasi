import "../styles/signup.css"
import { Link } from "react-router-dom";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

export function Signup() {
   const navigate = useNavigate();
   const [firstName, setFirstName] = useState('')
   const [lastName, setLastName] = useState('')
   const [homeAddress, setHomeAddress] = useState('')
   const [emailAddress, setEmailAddress] = useState('')
   const [password, setPassword] = useState('')
   const [emailExists, setEmailExists] = useState(false); // Dodato stanje za provjeru emaila

   useEffect(() => {
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

   useEffect(() => {
      if (emailAddress.length > 0) {
        fetch('/api/moderators')
            .then((response) => response.json())
            .then((registriraniModeratori) => {
               const duplikat = registriraniModeratori.some(
                  (moderator) => moderator.moderatorEmail === emailAddress );
               setEmailExists(duplikat);
            });
      }
   }, [emailAddress]);

   useEffect(() => {
      if (emailAddress.length > 0) {
        fetch('/api/trgovinas')
            .then((response) => response.json())
            .then((registriranaTrgovina) => {
               const duplikat = registriranaTrgovina.some(
                  (trgovina) => trgovina.trgovinaEmail === emailAddress );
               setEmailExists(duplikat);
            });
      }
   }, [emailAddress]);

   useEffect(() => {
      if (emailAddress.length > 0) {
        fetch('/api/administrators')
            .then((response) => response.json())
            .then((registriraniAdministratori) => {
               const duplikat = registriraniAdministratori.some(
                  (administrator) => administrator.administratorEmail === emailAddress );
               setEmailExists(duplikat);
            });
      }
   }, [emailAddress]);


   function saveNoviClan(e){
      
      e.preventDefault();

      if (emailExists) {
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
  
      return fetch('/api/kupacs', options)
         .then(response => {
            if (response.ok) {
               return response.json();
            }
         }).then(data => {
            localStorage.setItem('token', data.token);
            localStorage.setItem("id", data.id);
            localStorage.setItem('role', data.role);
            navigate('/home');
            alert("Uspješna registracija!");
         })  
   }

   function isValid() {
      //vec koristena adresa
      return firstName.length > 0 && lastName.length > 0 && emailAddress.length > 0
      && password.length > 0 && homeAddress.length > 0 && !emailExists ;
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
                     onChange={(e) => setEmailAddress(e.target.value)}/>
                      
                  <input type="password" placeholder="Password" id="pass" className="signup-inputs"  name="password" value={password}
                     onChange={(e) => setPassword(e.target.value)}/>
                     {emailExists && (
                        <p style={{ color: "red", marginTop: "4px" ,  marginBottom: "4px", fontWeight: "bold" }}>
                           This e-mail address is already in use!
                        </p>
                        )}
                  <button type="submit" className="signup-buttons" disabled={ !isValid() }>Sign up</button>
                  <Link to="/login">
                     <button id="Back" type="submit" className="signup-buttons">Back to Sign in</button>
                  </Link>
               </form>
            </div>
         </div>
      </div>
   
   );

}