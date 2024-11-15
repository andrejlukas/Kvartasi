import { Link } from "react-router-dom";
import logo from '../assets/MojKvart.png'
import kosarica from '../assets/kosarica.png'
import '../styles/Navbar.css'

export function Navbar() {

    const handleLogout = () => {
        localStorage.removeItem('token');
        window.location.href = '/'; 
     };
   
    
   return (
      <div className="header-container">
         <div id='najvanjski'>
                <nav className="navbar navbar-expand-lg  w-100" id="Vanjski">
                    <div className="container-fluid d-flex justify-content-between w-100" id="unutarnji">
                        <img className="navbar-brand" src={logo} alt="Logo" style={{ width: '150px' }} />

                        <form className="d-flex mx-auto" role="search" id="searchdiv">
                            <input className="form-control me-2" type="search" placeholder="Pretraži" aria-label="Pretraži" />
                            <button className="btn btn-outline-success" type="submit">Pretraži</button>
                        </form>

                        <div id="obruc" className="d-flex align-items-center ms-auto">
                            <a href="/korisnickiRacun" className="me-3" id="MojRacun">Moj račun</a>
                            <a href="#" onClick={handleLogout} className="me-3" id="OdjaviSe">Odjava</a>
                            <img  id='kosarica' className="img-fluid" src={kosarica} alt="Shopping Cart" />
                        </div>
                    </div>
                </nav>

                <nav className="navbar navbar-expand-lg w-100" id='drugiNav'>
                    <div className="container-fluid d-flex justify-content-between w-100 flex-nowrap" id="drugiNavUnut">
                        <ul className="navbar-nav d-flex justify-content-around w-100 flex-nowrap" id="drugiNavLista">
                            <li className="nav-item">
                                <a href="/home/kvart" className="nav-link">Kvart</a>
                            </li>
                            <li className="nav-item">
                                <a href="/home/popisTrgovina" className="nav-link">Popis trgovina</a>
                            </li>
                            <li className="nav-item">
                                <a href="/home/ponude" className="nav-link">Ponude i promocije</a>
                            </li>
                            <li className="nav-item">
                                <a href="/home/dogadaji" className="nav-link">Događaji</a>
                            </li>
                        </ul>
                    </div>
                </nav>
         </div>
      </div>
   );
}