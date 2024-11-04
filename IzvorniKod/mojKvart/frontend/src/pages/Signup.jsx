import "../styles/signup.css"
import { Link } from "react-router-dom";


export function Signup() {
   
   return (
      <div className="signup-container">
         <div className="container">
            <div className="form-box">
               <h2>Sign up</h2>
               <form action="#">
                  <div className="form-group">
                     <input type="text" placeholder="First name" className="signup-inputs" />
                     <input type="text" placeholder="Last name" className="signup-inputs"/>
                  </div>
                  <input type="text" placeholder="Home address" className="signup-inputs" />
                  <input type="email" placeholder="Email address" className="signup-inputs" />
                  <input type="password" placeholder="Password" className="signup-inputs" />
                  <button type="submit" className="signup-buttons">Sign up</button>
                  <Link to="/login">
                     <button className="signup-buttons" >Back to Sign in</button>
                  </Link>
               </form>
        </div>
      </div>
      </div>
   
   );

}