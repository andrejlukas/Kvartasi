import { Navbar } from "../components/Navbar";
import React, { useEffect, useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import "../styles/popisTrgovina.css";


export function PopisTrgovina() {
  const [shops, setShops] = useState([]); 
  const [error, setError] = useState(null);
  
  useEffect(() => {
    const token = localStorage.getItem('token');
    const options = {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    }
    fetch(`/api/trgovinas`, options)
      .then((response) => {
        if (!response.ok) {
          throw new Error("Neuspjesno dohvacanje trgovina.");
        }
        return response.json();
      })
      .then((data) => {
        setShops(data);
      })
      .catch((error) => {
        setError(error.message);
      });
  }, []);//samo se jednom dohvate sve trgovine

    const isShopOpen = (openingTime, closingTime) => { //funkcija koja vraca true ili false ovisno o tome radi li 
      const now = new Date(); 
      const [openHour, openMinute] = openingTime.split(':').map(Number);
      const [closeHour, closeMinute] = closingTime.split(':').map(Number);

      const openTime = new Date();
      openTime.setHours(openHour, openMinute, 0, 0); 

      const closeTime = new Date();
      closeTime.setHours(closeHour, closeMinute, 0, 0); 

      return now >= openTime && now <= closeTime;
    };
  return (
    <div>
      <Navbar />
        <div className="content-wrapper-shop">
          <div className="container-shops mt-4">
            {error ? (
              <p className="text-danger">{error}</p>
            ) : !shops ? (
              <p>Loading shops...</p>
            ) : (
              <div className="shop-shop-row">
                <div className="filters">
                    <strong><p>FILTRIRAJ</p></strong>
                    <p>filter po cijeni</p><p>filter po kategorijama</p>
                    {/* jos nema funkcionalnosti filtera */}
                </div>

                <div id="shops" className="shop-section">
                  <div className="row-shops">
                    {shops.length > 0 ? (
                      shops.map((shop) => (
                        <div key={shop.trgovinaId} className="my-card-wrapper">
                          <a href={`/home/popistrgovina/${shop.trgovinaId}`} className="nav-link">
                            <div className="cards shop-card">
                              <img 
                                src={shop.trgovinaSlika} 
                                className="card-img-top-shop" 
                                alt={shop.trgovinaNaziv} 
                              />
                              <div className="card-body-shop">
                                <div className="card-header-shop">
                                  <h5 className="card-title-shop">{shop.trgovinaNaziv}</h5>
                                  <span 
                                    className={`status-indicator ${isShopOpen(shop.trgovinaRadnoVrijemeOd, shop.trgovinaRadnoVrijemeDo) ? 'otvoreno' : 'zatvoreno'}`}
                                    style={{ color: isShopOpen(shop.trgovinaRadnoVrijemeOd, shop.trgovinaRadnoVrijemeDo) ? 'green' : 'red', fontSize: "18px" }}
                                  >
                                    {isShopOpen(shop.trgovinaRadnoVrijemeOd, shop.trgovinaRadnoVrijemeDo) ? 'OTVORENO' : 'ZATVORENO'}
                                  </span>
                                </div>
                                <p className="card-text location-text">{shop.trgovinaLokacija}</p>
                                <span>avg stars</span>
                                <p className="card-text hours-text">
                                  Radno vrijeme: <strong>{shop.trgovinaRadnoVrijemeOd} - {shop.trgovinaRadnoVrijemeDo}</strong>
                                </p>
                              </div>
                            </div>
                          </a>
                        </div>
                      ))
                    ) : (
                      <p>No shops available.</p>
                    )}
                  </div>
                </div>
              </div>
            )}
          </div>
        </div>
    </div>
  );
}