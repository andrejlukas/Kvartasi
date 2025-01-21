import React, { useEffect, useState } from "react";
import { Navbar } from "../../components/ModeratorNavbar";
import "bootstrap/dist/css/bootstrap.min.css";
import "../../styles/UpravljanjeKupcima.css";

export function ModeratorUpravljanjeKupcima() {
  const [customersType, setCustomersType] = useState("nonSuspended");
  const [error, setError] = useState("");
  const [suspendedCustomers, setSuspendedCustomers] = useState([]);
  const [nonSuspendedCustomers, setNonSuspendedCustomers] = useState([]);
  const [customers, setCustomers] = useState([]);

  useEffect(() => {
    const token = localStorage.getItem("token");
    const options = {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    };
  
    // Dohvati suspendirane kupce
    fetch("/api/kupacs", options)
      .then(async (response) => {
        if (!response.ok) {
          const text = await response.text();
          throw new Error(text);
        }
        return response.json();
      })
      .then((data) => {
        const filteredSuspended = data.filter(
            (customer) => customer.kupacStatus == "S"
          );
          setSuspendedCustomers(filteredSuspended);
          if (customersType === "suspended") {
            setCustomers(filteredSuspended);
          }
      })
      .catch((error) => {
        setError(error.message);
      });
  
    // Dohvati nesuspendirane kupce
    fetch("/api/kupacs", options)
      .then(async (response) => {
        if (!response.ok) {
          const text = await response.text();
          throw new Error(text);
        }
        return response.json();
      })
      .then((data) => {
        const filteredNonSuspended = data.filter(
          (customer) => customer.kupacStatus !== "S"
        );
        setNonSuspendedCustomers(filteredNonSuspended);
        if (customersType === "nonSuspended") {
          setCustomers(filteredNonSuspended);
        }
      })
      .catch((error) => {
        setError(error.message);
      });
  }, [customersType]); // Dodajte sve potrebne zavisnosti
  
  const handleCustomersTypeChange = (e) => {
    const selectedType = e.target.value;
    setCustomersType(selectedType);
    if (selectedType === "nonSuspended") {
      setCustomers(nonSuspendedCustomers);
    } else {
      setCustomers(suspendedCustomers);
    }
  };

  function suspendCustomer(customerData) {
    const token = localStorage.getItem("token");
    const options = {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ ...customerData, kupacStatus: "S" }),
    };

    fetch(`/api/kupacs/${customerData.kupacId}`, options)
      .then(async (response) => {
        if (!response.ok) {
          const text = await response.text();
          throw new Error(text);
        }
      })
      .then(() => {
        setNonSuspendedCustomers((prevCustomers) =>
          prevCustomers.filter((customer) => customer.kupacId !== customerData.kupacId)
        );
        setSuspendedCustomers((prevCustomers) => [...prevCustomers, { ...customerData, kupacStatus: "S" }]);
        if (customersType === "nonSuspended") {
          setCustomers((prevCustomers) =>
            prevCustomers.filter((customer) => customer.kupacId !== customerData.kupacId)
          );
        }
      })
      .catch((error) => {
        setError(error.message);
      });
  }

  const getStatusText = (status) => {
    switch (status) {
      case "S":
        return "Suspendiran";
      case "V":
        return "Verificiran";
      case "N":
        return "Neverificiran";
      default:
        return "Nepoznat status";
    }
  };

  return (
    <div>
  <div id="vani2" style={{ minHeight: "100vh" }}>
    <Navbar />
    <div id="customers-alt1" className="customer-section-alt1">
      <div id="customerController1">
        <label>
          <input
            type="radio"
            name="customersType"
            value="nonSuspended"
            checked={customersType === "nonSuspended"}
            onChange={handleCustomersTypeChange}
          />
          Nesuspendirani kupci
        </label>
        <label>
          <input
            type="radio"
            name="customersType"
            value="suspended"
            checked={customersType === "suspended"}
            onChange={handleCustomersTypeChange}
          />
          Suspendirani kupci
        </label>
      </div>
      <div className="row1">
        {customers.length > 0 ? (
          customers.map((customer) => (
            <div
              id="customersbox1"
              key={customer.kupacId}
              className="col-lg-4 col-md-6 col-12 mb-3-alt"
            >
              <div className="card-alt customer-card-alt1">
                <div className="card-body-alt1">
                  <h5 className="card-title-alt1">Kupac ID: {customer.kupacId}</h5>
                  <p className="card-text-alt1">
                    <strong>Ime:</strong> {customer.kupacIme}
                  </p>
                  <p className="card-text-alt1">
                    <strong>Prezime:</strong> {customer.kupacPrezime}
                  </p>
                  <p className="card-text-alt1">
                    <strong>Email:</strong> {customer.kupacEmail}
                  </p>
                  <p className="card-text-alt1">
                    <strong>Status:</strong> {getStatusText(customer.kupacStatus)}
                  </p>
                  {customersType === "nonSuspended" && (
                    <button
                      className="add-to-cart-btn-no"
                      onClick={() => suspendCustomer(customer)}
                    >
                      Suspendiraj kupca
                    </button>
                  )}
                </div>
              </div>
            </div>
          ))
        ) : (
          <p>Nema kupaca u ovoj kategoriji.</p>
        )}
      </div>
    </div>
  </div>
</div>

  );
}
