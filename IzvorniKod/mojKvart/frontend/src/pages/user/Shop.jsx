import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { Navbar } from "../../components/Navbar";
import { MapContainer, TileLayer, Marker } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import "../../styles/Shop.css";
import "bootstrap/dist/css/bootstrap.min.css";

export function Shop() {
  const { email } = useParams();
  const [shop, setShop] = useState(null);
  const [products, setProducts] = useState([]);
  const [attributes, setAttributes] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem("token");
    const options = {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    };
    if (email) {
      fetch(`/api/trgovinas/${email}`, options)
        .then((response) => {
          if (!response.ok) {
            throw new Error("Neuspješno dohvatanje trgovine.");
          }
          return response.json();
        })
        .then((data) => {
          setShop(data);
        })
        .catch((error) => {
          setError(error.message);
        });
    }
  }, [email]);

  useEffect(() => {
    if (shop) {
      const token = localStorage.getItem("token");
      const options = {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      };

      fetch(`/api/proizvods/approved/${shop.trgovinaId}`, options)
        .then((response) => {
          if (!response.ok) {
            throw new Error("Neuspješno dohvatanje proizvoda.");
          }
          return response.json();
        })
        .then((data) => {
          setProducts(data);
        })
        .catch((error) => {
          setError(error.message);
        });

      fetch(`/api/atributs`, options)
        .then((response) => {
          if (!response.ok) {
            throw new Error("Neuspješno dohvatanje atributa.");
          }
          return response.json();
        })
        .then((data) => {
          const trgovinaAtributi = data.filter((atribut) =>
            shop.imaAtributeAtributs.includes(atribut.atributId)
          );
          setAttributes(trgovinaAtributi);
        })
        .catch((error) => {
          setError(error.message);
        });
    }
  }, [shop]);

  // Parse the location coordinates
  const getCoordinates = () => {
    if (!shop || !shop.trgovinaLokacija) return null;
    const [lat, lng] = shop.trgovinaLokacija.split(",").map(Number);
    return { lat, lng };
  };

  const coordinates = getCoordinates();

  return (
    <div>
      <Navbar />
      <div className="content-wrapper">
        <div className="container mt-4">
          {error ? (
            <p className="text-danger">{error}</p>
          ) : !shop ? (
            <p>Loading shop details...</p>
          ) : (
            <div className="shop-product-row">
              <div id="shops" className="shop-info">
                <img
                  src={shop.trgovinaSlika}
                  alt={shop.trgovinaNaziv}
                  className="img-fluid mb-3 shop-image"
                />
                <div id="pomoc">
                  <h2 id="nazivTrgovine">{shop.trgovinaNaziv || "N/A"}</h2>
                  <p id="opis">
                    <strong></strong> {shop.trgovinaOpis || "Nije dostupan"}
                  </p>
                </div>
                <div className="divider"></div>

                <p>
                  <strong>Adresa:</strong> {shop.trgovinaLokacija || "Nije specifirano"}
                </p>
                <p>
                  <strong>Radno vrijeme: </strong>
                  {shop.trgovinaRadnoVrijemeOd && shop.trgovinaRadnoVrijemeDo
                    ? `${shop.trgovinaRadnoVrijemeOd} - ${shop.trgovinaRadnoVrijemeDo}`
                    : "Nije specifirano"}
                </p>
                <p>
                  <strong>Kategorija: </strong> {shop.trgovinaKategorija || "Nije specifirano"}
                </p>
                <p>
                  <strong>Email: </strong> {shop.trgovinaEmail || "Nije dostupan"}
                </p>

                {coordinates && (
                  <div className="shop-map">
                    <MapContainer
                      center={[coordinates.lat, coordinates.lng]}
                      zoom={15}
                      style={{ height: "400px", width: "100%" }}
                    >
                      <TileLayer
                        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                      />
                      <Marker position={[coordinates.lat, coordinates.lng]} />
                    </MapContainer>
                  </div>
                )}

                <div id="atributs">
                  {attributes.length > 0 && (
                    <>
                      <div id="dividerr"></div>
                      <ul className="no-bullets">
                        {attributes.map((atribut) => (
                          <li key={atribut.atributId}>
                            <strong>{atribut.atributNaziv}</strong> {atribut.atributOpis}
                          </li>
                        ))}
                      </ul>
                    </>
                  )}
                </div>
              </div>

              <div id="products" className="product-section">
                <div className="row">
                  {products.length > 0 ? (
                    products.map((product) => (
                      <div key={product.proizvodId} className="col-md-6 mb-3">
                        <div className="card product-card">
                          <img
                            src={product.proizvodSlika}
                            className="card-img-top"
                            alt={product.proizvod_naziv}
                          />
                          <div className="card-body">
                            <h5 className="card-title">{product.proizvodNaziv}</h5>
                            <p className="card-text">{product.proizvodOpis}</p>
                            <div id="gumbcijena">
                              <p className="price">€{product.proizvodCijena}</p>
                              <button className="add-to-cart-btn">Dodaj u košaricu</button>
                            </div>
                          </div>
                        </div>
                      </div>
                    ))
                  ) : (
                    <p>Nisu dostupni proizvodi.</p>
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
