import { Navbar } from "../components/Navbar";
import React, { useEffect, useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import "../styles/PopisTrgovina.css";

export function PopisTrgovina() {
  const [shops, setShops] = useState([]);
  const [categories, setCategories] = useState([]);
  const [atributs, setAtributs] = useState([]);
  const [error, setError] = useState(null);
  const [activePopover, setActivePopover] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem('token');
    const options = {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
    };

    fetch(`/api/trgovinas`, options)
      .then((response) => {
        if (!response.ok) {
          throw new Error("Neuspješno dohvaćanje trgovina.");
        }
        return response.json();
      })
      .then((data) => {
        setShops(data);
        setCategories(new Set(data.map(trgovina => trgovina.trgovinaKategorija)));
      })
      .catch((error) => {
        setError(error.message);
      });
    
    fetch("/api/atributs", options)
      .then((response) => {
        if (!response.ok) {
          throw new Error("Neuspješno dohvaćanje dodatnih značajki.");
        }
        return response.json();
      })
      .then((data) => {
        setAtributs(new Set(data));
      })
      .catch((error) => {
        setError(error.message);
      });
  }, []);

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

  function openTheFilter(popoverDiv) {
    const element = document.getElementById("window");
    element.style.cursor = "not-allowed";
    element.style.opacity = 0.5;
    setActivePopover(activePopover === popoverDiv ? null : popoverDiv);
  }

  function closeTheFilter() {
    const element = document.getElementById("window");
    element.style.cursor = "auto";
    element.style.opacity = 1;
    setActivePopover(null);
  }

  function filterByDistance() {
    window.location.reload();
  }

  function filterByCategories() {
    window.location.reload();
    //console.log(categories);
  }

  function filterByAtributs() {
    window.location.reload();
    //console.log(atributs);
  }


  return (
    <div>
      <div id="window">
        <Navbar />
        <div className="content-wrapper-shop">
          <div className="container-shops mt-4">
            {error ? (
              <p className="text-danger">{error}</p>
            ) : !shops.length ? (
              <p>Loading shops...</p>
            ) : (
              <div className="shop-shop-row">
                <div className="filters">
                  <strong><p>FILTRIRAJ</p></strong>
                  <button onClick={() => openTheFilter("distanceDiv")}>Po udaljenosti</button>
                  <button onClick={() => openTheFilter("categoryDiv")}>Po kategorijama</button>
                  <button onClick={() => openTheFilter("atributsDiv")}>Po dodatnim značajkama</button>
                </div>

                <div id="shops" className="shop-section">
                  <div className="row-shops">
                    {shops.length > 0 ? (
                      shops.map((shop) => (
                        <div key={shop.trgovinaId} className="my-card-wrapper">
                          <a
                            href={`/home/popistrgovina/${shop.trgovinaId}`}
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
                                <p className="card-text location-text">
                                  {shop.trgovinaLokacija}
                                </p>
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

      {activePopover === "distanceDiv" && (
        <div id="distanceDiv" className="filterPopovers">
          <p>Želite li izlistati Vama najbliže trgovine?</p>
          <div className="YesNoButtons">
            <button onClick={() => filterByDistance()}>Da</button>
            <button onClick={() => closeTheFilter()}>Ne</button>
          </div>
        </div>
      )}

      {activePopover === "categoryDiv" && (
        <div id="categoryDiv" className="filterPopovers">
          <div className="filterItem">
            <input type="checkbox" name="category" value="category" />
            <label htmlFor="category">Prva kategorija</label>
            <br/>
          </div>
          <div className="YesNoButtons">
              <button onClick={() => filterByCategories()}>Pretraži</button>
              <button onClick={() => closeTheFilter()}>Odustani</button>
            </div>
        </div>
      )}

      {activePopover === "atributsDiv" && (
        <div id="atributsDiv" className="filterPopovers">
          <div className="filterItem">
            <input type="checkbox" name="atribut" value="atribut" />
            <label htmlFor="atribut">Prvi atribut</label>
            <br/>
          </div>
          <div className="YesNoButtons">
              <button onClick={() => filterByAtributs()}>Pretraži</button>
              <button onClick={() => closeTheFilter()}>Odustani</button>
            </div>
        </div>
      )}
    </div>
  );
}