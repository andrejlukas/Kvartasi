import "../styles/signup.css"
import { Link } from "react-router-dom";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

export function Signup() {
   const navigate = useNavigate();
   const [firstName, setFirstName] = useState('')
   const [lastName, setLastName] = useState('')
   const [homeAddress, setHomeAddress] = useState('')
   const [emailAddress, setEmailAddress] = useState('')
   const [password, setPassword] = useState('')
   const [emailExists, setEmailExists] = useState(false); // Dodato stanje za provjeru emaila

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
                     onChange={(e) => setEmailAddress(e.target.value)}/>
                      
                  <input type="password" placeholder="Password" id="pass" className="signup-inputs"  name="password" value={password}
                     onChange={(e) => setPassword(e.target.value)}/>
                     {emailExists && (
                        <p style={{ color: "red", marginTop: "4px" ,  marginBottom: "4px", fontWeight: "bold" }}>
                           This email is occupied!
                        </p>
                        )}
                  <button type="submit" className="signup-buttons" >Sign up</button>
                  <Link to="/">
                     <button id="Back" type="submit" className="signup-buttons">Back to Sign in</button>
                  </Link>
               </form>

               <a href="http://localhost:8080/oauth2/authorization/google">
                  Sign in with Google
               </a>
            </div>
         </div>
      </div>
   
   );

}