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
    <div>
      <h1> Gilded Rose Inventory </h1>

      <InventoryTable items={items} />
      <button
       type="button" 
       onClick={advanceDay}
       disabled={isLoading}
       >

        {isLoading ? "Updating..." : "Advance One Day"}
      </button>

      <AddItemForm onAddItem={handleAddItem} />
      </div>
  );  
}

export default App;