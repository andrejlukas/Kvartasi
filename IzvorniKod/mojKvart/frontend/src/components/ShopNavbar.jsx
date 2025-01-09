import logo from '../assets/MojKvart.png'
import '../styles/NavBar.css'

export function Navbar() {

    const handleLogout = () => {
        localStorage.clear();
        window.location.href = '/'; 
    };

    
   return (
      <div className="header-container">
         <div id='najvanjski'>
                <nav className="navbar navbar-expand-lg  w-100" id="Vanjski">
                    <div className="container-fluid d-flex justify-content-between w-100" id="unutarnji">
                        <img className="navbar-brand" src={logo} alt="Logo" style={{ width: '150px' }} />

                        <div id="obruc" className="d-flex align-items-center ms-auto">
                            <a href="/racunTrgovine" className="me-3" id="MojRacun">Moj račun</a>
                            <a href="#" onClick={handleLogout} className="me-3" id="OdjaviSe">Odjava</a>
                        </div>
                    </div>
                </nav>

                <nav className="navbar navbar-expand-lg w-100" id='drugiNav'>
                    <div className="container-fluid d-flex justify-content-between w-100 flex-nowrap" id="drugiNavUnut">
                        <ul className="navbar-nav d-flex justify-content-around w-100 flex-nowrap" id="drugiNavLista">
                            <li className="nav-item">
                                <a href="/trgovina/home/proizvodi" className="nav-link">Moji proizvodi</a>
                            </li>
                            <li className="nav-item">
                                <a href="/trgovina/home/narudzbe" className="nav-link">Narudžbe</a>
                            </li>
                            <li className="nav-item">
                                <a href="/trgovina/home/ponude" className="nav-link">Ponude i promocije</a>
                            </li>
                            <li className="nav-item">
                                <a href="/trgovina/home/dogadaji" className="nav-link">Događaji</a>
                            </li>
                        </ul>
                    </div>
                </nav>
         </div>
      </div>
   );
}