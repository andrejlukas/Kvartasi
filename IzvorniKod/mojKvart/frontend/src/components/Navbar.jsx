import { useEffect, useState } from 'react';
import logo from '../assets/MojKvart.png'
import kosarica from '../assets/kosarica.png'
import '../styles/NavBar.css'
import '../styles/Search.css'

export function Navbar() { 
    const [resultsType, setResultsType] = useState("shops");
    const [resultShops, setResultShops] = useState([]);
    const [resultProducts, setResultProducts] = useState([]);
    const [tempSearchInput, setTempSearchInput] = useState("");
    const [searchInput, setSearchInput] = useState("");
    const [error, setError] = useState("");

    useEffect(() => {
        const token = localStorage.getItem("token");
        const options = {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        }

        fetch(`/api/trgovinas/getBySearch/${searchInput}`, options)
            .then(async (response) => {
                if (!response.ok) {
                    const text = await response.text();
                    throw new Error(text);
                }
                return response.json();
            })
            .then(data => {
                setResultShops(data);
                setError("");
            })
            .catch((error) => {
                setError("Error fetching search results: ", error);
            });

        fetch(`/api/proizvods/getBySearch/${searchInput}`, options)
            .then(async (response) => {
                if (!response.ok) {
                    const text = await response.text();
                    throw new Error(text);
                }
                return response.json();
            })
            .then(data => {
                setResultProducts(data);
                setError("");
            })
            .catch((error) => {
                setError("Error fetching search results: ", error);
            });

    }, [searchInput])

    const searchResults = (e) => {
        e.preventDefault();
        setSearchInput(tempSearchInput);

        document.getElementById("root").style.cursor = "not-allowed";
        document.getElementById("searchPopup").style.display = "flex";
    };

    const handleResultsTypeChange = (e) => {
        setResultsType(e.target.value);
    }

    const handleCloseSearchResults = () => {
        document.getElementById("searchPopup").style.display = "none";
        document.getElementById("root").style.cursor = "auto";
    }

    const isShopOpen = (openingTime, closingTime) => {
        const now = new Date();
        const [openHour, openMinute] = openingTime.split(':').map(Number);
        const [closeHour, closeMinute] = closingTime.split(':').map(Number);
    
        const openTime = new Date();
        openTime.setHours(openHour, openMinute, 0, 0);
    
        const closeTime = new Date();
        closeTime.setHours(closeHour, closeMinute, 0, 0);
    
        return now >= openTime && now <= closeTime;
      };

    const handleLogout = () => {
        localStorage.clear();
        window.location.href = '/'; 
    };
   
    
   return (
        <div className="header-container">
            <div id='najvanjski'>
                <nav className="navbar navbar-expand-lg  w-100" id="Vanjski">
                    <div className="container-fluid d-flex justify-content-between w-100" id="unutarnji">
                        <img className="navbar-brand" id="logo" src={logo} alt="Logo" style={{ width: '150px' }} />

                        <form className="d-flex mx-auto" role="search" id="searchdiv" onSubmit={searchResults}>
                            <input className="form-control me-2"
                                   type="search" 
                                   placeholder="Pretraži"
                                   aria-label="Pretraži"
                                   value={tempSearchInput}
                                   onChange={(e) => setTempSearchInput(e.target.value)} />
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

            <div id="searchPopup">
                <div id="searchOptions">
                    <label>
                        <input
                        type="radio"
                        name="resultsType"
                        value="shops"
                        checked={resultsType === "shops"}
                        onChange={handleResultsTypeChange}
                        />
                        Trgovine
                    </label>
                    <label>
                        <input
                        type="radio"
                        name="resultsType"
                        value="products"
                        checked={resultsType === "products"}
                        onChange={handleResultsTypeChange}
                        />
                        Proizvodi
                    </label>
                    <button id="closeSearch" onClick={handleCloseSearchResults}>Zatvori</button>
                </div>
                {error && <p>{error}</p>}
                <div id="searchResults">
                    {resultsType === "shops" ? (
                        resultShops && resultShops.length > 0 ? (
                            resultShops.map((shop) => (
                                <div key={shop.trgovinaId} className="my-card-wrapper">
                                <a
                                    href={`/home/popistrgovina/${shop.trgovinaEmail}`}
                                    className="nav-link"
                                >
                                    <div className="cards shop-card">
                                        <img
                                        src={shop.trgovinaSlika}
                                        className="card-img-top-shop"
                                        alt={shop.trgovinaNaziv}
                                        />
                                    <div className="card-body-shop">
                                        <div className="card-header-shop">
                                        <h5 className="card-title-shop">
                                            {shop.trgovinaNaziv}
                                        </h5>
                                        <span
                                            className={`status-indicator ${
                                            isShopOpen(
                                                shop.trgovinaRadnoVrijemeOd,
                                                shop.trgovinaRadnoVrijemeDo
                                            )
                                                ? "otvoreno"
                                                : "zatvoreno"
                                            }`}
                                            style={{
                                            color: isShopOpen(
                                                shop.trgovinaRadnoVrijemeOd,
                                                shop.trgovinaRadnoVrijemeDo
                                            )
                                                ? "green"
                                                : "red",
                                            fontSize: "18px",
                                            }}
                                        >
                                            {isShopOpen(
                                            shop.trgovinaRadnoVrijemeOd,
                                            shop.trgovinaRadnoVrijemeDo
                                            )
                                            ? "OTVORENO"
                                            : "ZATVORENO"}
                                        </span>
                                        </div>
                                        <span>avg stars</span>
                                        <p className="card-text hours-text">
                                        Radno vrijeme:{" "}
                                        <strong>
                                            {shop.trgovinaRadnoVrijemeOd} -{" "}
                                            {shop.trgovinaRadnoVrijemeDo}
                                        </strong>
                                        </p>
                                    </div>
                                    </div>
                                </a>
                                </div>
                            ))) : <p>Nema traženih trgovina.</p>
                    ) : (
                        resultProducts && resultProducts.length > 0 ? (
                            resultProducts.map((product) => (
                                <div id="productsbox" key={product.proizvodId} className="col-lg-4 col-md-6 col-12 mb-3-alt">
                                    <div className="card-alt product-card-alt">
                                        <img 
                                          src={product.proizvodSlika} 
                                          className="card-img-top-alt" 
                                          alt={product.proizvod_naziv} 
                                        />
                                        <div className="card-body-alt">
                                            <h5 className="card-title-alt">{product.proizvodNaziv}</h5>
                                            <p className="card-text-alt">{product.proizvodOpis}</p>
                                            <div id="gumbcijena-alt">
                                                <p className="price-alt">€{product.proizvodCijena}</p>
                                                <button className="add-to-cart-btn-alt">Dodaj u košaricu</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            ))) : ( <p>Nema traženih proizvoda.</p> )
                    )}
                </div>
            </div>
      </div>
   );
}