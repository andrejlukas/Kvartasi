import { Navbar } from "../../components/Navbar";
import React, { useEffect, useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import "../../styles/PopisTrgovina.css";
import L from 'leaflet';

export function PopisTrgovina() {
  const [shops, setShops] = useState([]);
  const [categories, setCategories] = useState([]);
  const [selectedCategories, setSelectedCategories] = useState([]);
  const [shopsToRenderOut, setShopsToRenderOut] = useState([]);

  const [email, setEmail] = useState(null);
  const [address, setAddress] = useState(null);
  const [activePopover, setActivePopover] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem('token');
    let options = {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
    };

    fetch(`/api/trgovinas`, options)
      .then(async (response) => {
        if (!response.ok) {
          const text = await response.text();
          throw new Error(text);
        }
        return response.json();
      })
      .then((data) => {
        setShops(data);
        setCategories([...new Set(data.map(trgovina => trgovina.trgovinaKategorija))]);
      })
      .catch((error) => {
        setError(error.message);
      });

    options = {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ oneLiner: token })
    }

    fetch('/api/tokens/claims', options)
        .then(async response => {
            if (!response.ok) {
              const text = await response.text();
              throw new Error(text);
            }
            return response.json();
        })
        .then(data => {
            setEmail(data.email);
        })
        .catch(error => setError(error.message));
  }, []);

  useEffect(() => {
    if (!email) return;

    const token = localStorage.getItem('token');
    const options = {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    }
    
    fetch(`/api/kupacs/${email}`, options)
      .then(async (response) => {
          if (!response.ok) {
              const text = await response.text();
            throw new Error(text);
          }
          return response.json();
      })
      .then((data) => {
          setAddress(data.kupacAdresa);
      })
      .catch((error) => {
          setError(error.message);
      });
  }, [email]);

  useEffect(() => {
    if(categories.length > 0 && localStorage.getItem("filter") === null) {
      localStorage.setItem("filter", JSON.stringify({ distance: false, categories: categories }));
      return;
    }
  }, [categories]);

  useEffect(() => {
    if(!address) return;
    if(!shops) return;
    if(localStorage.getItem("filter") !== null) applyFilters();
  }, [address, shops]);
  

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

  const distanceToShop = (myLocation, shopLocation) => {
    const coord1 = L.latLng(myLocation.split(",")[0], myLocation.split(",")[1]);
    const coord2 = L.latLng(shopLocation.split(",")[0], shopLocation.split(",")[1]);
    return Math.round(coord1.distanceTo(coord2)) / 1000; // distance in km
  }

  const handleCategoryChange = (event) => {
    const { value, checked } = event.target;
    setSelectedCategories((prev) => {
      if (checked && !prev.includes(value)) {
        return [...prev, value];
      } else if (!checked) {
        return prev.filter((category) => category !== value);
      }
      return prev;
    });
  };

  const openTheFilter = (popoverDiv) => {
    const element = document.getElementById("window");
    element.style.cursor = "not-allowed";
    element.style.opacity = 0.5;
    setActivePopover(activePopover === popoverDiv ? null : popoverDiv);
  };

  const closeTheFilter = () => {
    const element = document.getElementById("window");
    element.style.cursor = "auto";
    element.style.opacity = 1;
    setActivePopover(null);
  };

  const startFiltering = (by) => {
    var data = JSON.parse(localStorage.getItem("filter"));
    if(by === "distanceYes") data.distance = true;
    else if(by === "distanceNo") data.distance = false;
    else  data.categories = selectedCategories;
    localStorage.setItem("filter", JSON.stringify(data));
    closeTheFilter();
    window.location.reload();
  }

  const applyFilters= () => {
    const distanceYesNo = JSON.parse(localStorage.getItem("filter")).distance;
    const chosenCategories = JSON.parse(localStorage.getItem("filter")).categories;

    let filteredShops = chosenCategories.length === 0 ? [] : shops;
    if(filteredShops.length > 0) 
      filteredShops = filteredShops.filter((shop) => chosenCategories.indexOf(shop.trgovinaKategorija) > -1);

    if(filteredShops.length > 0 && distanceYesNo) 
      filteredShops = filteredShops.map((shop) => ({...shop, "distance": distanceToShop(address, shop.trgovinaLokacija)})).sort(compareShopsByDistance);
    
    setShopsToRenderOut(filteredShops);
  };

  const resetFilters = () => {
    localStorage.setItem("filter", JSON.stringify({ distance: false, categories: categories }));
    window.location.reload();
  };

  function compareShopsByDistance(shop1, shop2) {
    return shop1.distance - shop2.distance;
  }

  return (
    <div>
      <div id="window">
        <Navbar />
        <div className="content-wrapper-shop">
          <div className="container-shops mt-4">
            <div className="shop-shop-row">
              <div className="filters">
                <strong><p>FILTRIRAJ</p></strong>
                <button onClick={() => openTheFilter("distanceDiv")}>Po udaljenosti</button>
                <button onClick={() => openTheFilter("categoryDiv")}>Po kategorijama</button>
                <button onClick={() => resetFilters()}>Po početnim postavkama</button>
              </div>

              {error ? (
                <p className="text-danger">{error}</p>
              ) : !shopsToRenderOut ? (
                <p>Učitavanje trgovina...</p>
              ) : (
                <div id="shops" className="shop-section">
                  <div className="row-shops">
                    {address && shopsToRenderOut.length > 0 ? (
                      shopsToRenderOut.map((shop) => (
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
                                <p className="card-text location-text">
                                  {"Udaljeno od vas: " + distanceToShop(address, shop.trgovinaLokacija) + " km"}
                                </p>
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
                      <p>Nema trgovina koje odgovaraju vašem filteru.</p>
                    )}
                  </div>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>

      {activePopover === "distanceDiv" && (
        <div id="distanceDiv" className="filterPopovers">
          <p>Želite li izlistati Vama najbliže trgovine?</p>
          <div className="YesNoButtons">
            <button onClick={() => startFiltering("distanceYes")}>Da</button>
            <button onClick={() => startFiltering("distanceNo")}>Ne</button>
          </div>
        </div>
      )}

      {activePopover === "categoryDiv" && (
        <div id="categoryDiv" className="filterPopovers">
          <p>Prikazivat će se trgovine čija</p><p>je kategorija makar jedna od odabranih!</p>
          {categories.length > 0 ? (categories.map(category => 
            <div className="filterItem">
              <input type="checkbox" 
                    name={`${category}`} 
                    value={`${category}`} 
                    onChange={handleCategoryChange}
              />
              <label htmlFor={`${category}`}>{`${category}`}</label>
              <br/>
            </div>)) : <p>Nema dostupnih kategorija</p> }
          <div className="YesNoButtons">
              <button onClick={() => startFiltering("categories")}>Pretraži</button>
              <button onClick={() => closeTheFilter()}>Odustani</button>
            </div>
        </div>
      )}
    </div>
  );
}