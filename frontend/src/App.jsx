import './App.css';
import { useState} from "react";
import InventoryTable from './components/InventoryTable';
import AddItemForm from './components/AddItemForm';
import {updateInventory} from './api/inventoryApi';

function App() {
  const [items, setItems] = useState([
    { name: "Normal Item", sellIn: 5, quality: 10 },
    { name: "Aged Brie", sellIn: 3, quality: 20 },
    { name: "Sulfuras", sellIn: 0, quality: 80 },
    { name: "Conjured", sellIn: 2, quality: 10 },
  ]);


  const [isLoading, setIsLoading] = useState(false);

  const handleAddItem = (newItem) => {
    setItems(previousItems => [
      ...previousItems,
      newItem
    ]);
  };

  const advanceDay = async () => {
    setIsLoading(true);

    try {

      const updatedItems = await updateInventory(items);
      setItems(updatedItems);

    } catch (error) {
        console.error("Failed to advance day:", error);
        alert("Error updating inventory:" + error.message);

      } finally {
        setIsLoading(false);
      }
  };

  return  (   
    <main className='app'>
      <header className='app-header'>
        <p className='eyebrow'>Inventory Management</p>
        <h1> Gilded Rose Inventory </h1>
        <p className='app-description'>
          Manage your inventory and update item quality each day.
        </p>
      </header>

      <section className='panel inventory-panel'>
        <div className='panel-toolbar'>
          <h2>Current Inventory</h2>

          <button
          className='primary-button'
          type='button'
          onClick={advanceDay}
          disabled={isLoading}
          >
            {isLoading ? "Updating..." : "Advance One Day"}
          </button>
        </div>

        <div className='table-scroll'>
          <InventoryTable items={items} />
        </div>
      </section>

      <section className='panel form-panel'>
        <AddItemForm onAddItem={handleAddItem} />
      </section>

    </main>
  );

}

export default App;