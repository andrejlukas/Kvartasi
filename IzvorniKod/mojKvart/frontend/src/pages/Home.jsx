import { Link } from "react-router-dom";
import 'bootstrap/dist/css/bootstrap.min.css';
import "../styles/home.css"
import logo from '../assets/MojKvart.png'
import kosarica from '../assets/kosarica.png'


export function Home(){
    return (
        <div>
            <div className="header-container">
                <div id='najvanjski'>
                <nav className="navbar navbar-expand-lg  w-100" id="Vanjski">
                    <div className="container-fluid d-flex justify-content-between w-100" id="unutarnji">
                        {/* Logo on the right */}
                        <img className="navbar-brand" src={logo} alt="Logo" style={{ width: '150px' }} />

                        {/* Search bar in the center */}
                        <form className="d-flex mx-auto" role="search" id="searchdiv">
                            <input className="form-control me-2" type="search" placeholder="Search" aria-label="Search" />
                            <button className="btn btn-outline-success" type="submit">Search</button>
                        </form>

                        {/* Account link and shopping cart icon on the right */}
                        <div className="d-flex align-items-center ms-auto">
                            <a href="/KorisnickiRacun" className="me-3" id="MojRacun">Moj račun</a>
                            <img  id='kosarica' className="img-fluid" src={kosarica} alt="Shopping Cart" />
                        </div>
                    </div>
                </nav>

                <nav className="navbar navbar-expand-lg w-100" id='drugiNav'>
                    <div className="container-fluid d-flex justify-content-between w-100 flex-nowrap" id="drugiNavUnut">
                        <ul className="navbar-nav d-flex justify-content-around w-100 flex-nowrap" id="drugiNavLista">
                            <li className="nav-item">
                                <Link to="#" className="nav-link active">Kvart</Link>
                            </li>
                            <li className="nav-item">
                                <Link to="#" className="nav-link">Popis trgovina</Link>
                            </li>
                            <li className="nav-item">
                                <Link to="#" className="nav-link">Ponude i promocije</Link>
                            </li>
                            <li className="nav-item">
                                <Link to="#" className="nav-link">Događaji</Link>
                            </li>
                        </ul>
                    </div>
                </nav>
                </div>
            </div>
            <div className="main-container"> {/* koristim main-container za sve osim header-a */}
                <h1 style={{textAlign:"center"}}>
                    Home - Uspješna prijava!
                </h1>
            </div>
            
        </div>
    );
}