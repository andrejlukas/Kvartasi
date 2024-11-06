import "../styles/signup.css"
import { Link } from "react-router-dom";
import { useState, useEffect } from "react";
import {useNavigate} from "react-router-dom";

export function Signup() {
   const navigate = useNavigate();
   const [firstName, setFirstName] = useState('')
   const [lastName, setLastName] = useState('')
   const [homeAddress, setHomeAddress] = useState('')
   const [emailAddress, setEmailAddress] = useState('')
   const [password, setPassword] = useState('')
   const [emailExists, setEmailExists] = useState(false); // Dodato stanje za proveru emaila

   useEffect(() => {
      if (emailAddress.length > 0) {
        fetch(`/api/kupacs`)
          .then((response) => response.json())
          .then((registriraniKorisnici) => {
            const duplikat = registriraniKorisnici.some(
              (korisnik) => korisnik.kupacEmail === emailAddress
            );
            setEmailExists(duplikat);
          });
      }
    }, [emailAddress]);

   function saveNoviClan(e){
      
      e.preventDefault();

      if (emailExists) {
         //alert("Email adresa je već korištena!");
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
  
      return fetch('/api/kupacs', options)
        .then(response => {
          if (response.ok) {
            navigate('/');
            alert("Uspješna registracija! Sada se možeš prijaviti")
            console.log("sve okej dodan")
          }
        });
  
     
   }

   function isValid() {
      //vec koristena adresa
      


      return firstName.length > 0 && lastName.length > 0 && emailAddress.length > 0
      && password.length > 0 && homeAddress.length > 0 && !emailExists ;
    }
  
   function handlerEmailAdress(e){
      setEmailAddress(e.target.value)
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
                  <input type="text" placeholder="Home address" className="signup-inputs"  name="homeAddress" value={homeAddress}
                     onChange={(e) => setHomeAddress(e.target.value)}/>
                  <input type="email" placeholder="Email address" className="signup-inputs"  name="emailAddress" value={emailAddress}
                     onChange={handlerEmailAdress}/>
                      
                  <input type="password" placeholder="Password" className="signup-inputs"  name="password" value={password}
                     onChange={(e) => setPassword(e.target.value)}/>
                     {emailExists && (
                        <p style={{ color: "red", marginTop: "4px" ,  marginBottom: "4px", fontWeight: "bold" }}>
                           Email adresa je već korištena!
                        </p>
                        )}
                  <button type="submit" className="signup-buttons" >Sign up</button>
                  <Link to="/login">
                     <button type="submit" disabled={!isValid()}className="signup-buttons" >Back to Sign in</button>
                  </Link>
               </form>
        </div>
      </div>
      </div>
   
   );

}