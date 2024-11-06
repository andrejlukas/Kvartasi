import "../styles/signup.css"
import { Link } from "react-router-dom";
import { useState } from "react";


export function Signup() {

   const [firstName, setFirstName] = useState('')
   const [lastName, setLastName] = useState('')
   const [homeAddress, setHomeAddress] = useState('')
   const [emailAddress, setEmailAddress] = useState('')
   const [password, setPassword] = useState('')
   const [id, setId] = useState(1)


   function saveNoviClan(e){
      
      e.preventDefault();
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
            //navigate('/students');
            console.log("sve okej dodan")
          }
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
                  <input type="text" placeholder="Home address" className="signup-inputs"  name="homeAddress" value={homeAddress}
                     onChange={(e) => setHomeAddress(e.target.value)}/>
                  <input type="email" placeholder="Email address" className="signup-inputs"  name="emailAddress" value={emailAddress}
                     onChange={(e) => setEmailAddress(e.target.value)}/>
                  <input type="password" placeholder="Password" className="signup-inputs"  name="password" value={password}
                     onChange={(e) => setPassword(e.target.value)}/>
                  <button type="submit" className="signup-buttons" >Sign up</button>
                  <Link to="/login">
                     <button type="submit" className="signup-buttons" >Back to Sign in</button>
                  </Link>
               </form>
        </div>
      </div>
      </div>
   
   );

}