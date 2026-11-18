/**
 * @vitest-environment jsdom
 */

import { test, expect, vi} from 'vitest';
import { render, screen, cleanup } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { afterEach } from 'vitest';
import AddItemForm from './AddItemForm';

afterEach(() => {
    cleanup();
});

test('adds an item with valid values', async () => {
    const onAddItem = vi.fn();
    const user = userEvent.setup();

    render(<AddItemForm onAddItem={onAddItem} />);

    await user.selectOptions(
        screen.getByLabelText('Item Type:'),
        'Aged Brie'
    );

    const sellInInput = screen.getByLabelText('SellIn:');
    await user.clear(sellInInput);
    await user.type(sellInInput, '7');

    const qualityInput = screen.getByLabelText('Quality:');
    await user.clear(qualityInput);
    await user.type(qualityInput, '20');

    await user.click(
        screen.getByRole('button', { name: 'Add Item' })
    );

    expect(onAddItem).toHaveBeenCalledWith({
        name: 'Aged Brie',
        sellIn: 7,
        quality: 20
    });
});

test('does not add an item when SellIn is empty', async () => {
    const onAddItem = vi.fn();
    const user = userEvent.setup();

    render(<AddItemForm onAddItem={onAddItem} />);

    const sellInInput = screen.getByLabelText('SellIn:');
    await user.clear(sellInInput);

    await user.clear(sellInInput);
    await user.click(
        screen.getByRole('button', { name: 'Add Item' })
    );

    expect(screen.getByRole('alert').textContent)
    .toContain('Please enter valid values');
    expect(onAddItem).not.toHaveBeenCalled();
});

test('sets Sulfuras quality to 80', async () => {
    const onAddItem = vi.fn();
    const user = userEvent.setup();

    render(<AddItemForm onAddItem={onAddItem} />);
    
    await user.selectOptions(
        screen.getByLabelText('Item Type:'),
        'Sulfuras'
    );

    const qualityInput = screen.getByLabelText('Quality:');

    expect(qualityInput.value).toBe('80');
    expect(qualityInput.disabled).toBe(true);

    await user.click(
        screen.getByRole('button', { name: 'Add Item' })
    );

    expect(onAddItem).toHaveBeenCalledWith({
        name: 'Sulfuras',
        sellIn: 5,
        quality: 80
    });
});