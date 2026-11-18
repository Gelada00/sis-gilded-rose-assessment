/**
 * @vitest-environment jsdom
 */

import { test, expect, TestRunner } from 'vitest';
import { render, screen } from '@testing-library/react';
import InventoryTable from './InventoryTable';

test('displays inventory items correctly', () => {
    const items = [
        {name: "Normal Item", sellIn: 5, quality: 10},
        {name: "Aged Brie", sellIn: -2, quality: 20},
    ];

    render(<InventoryTable items={items} />);

    expect(screen.getByText('Normal Item')).toBeTruthy();
    expect(screen.getByText('Aged Brie')).toBeTruthy();
    expect(screen.getByText('-2')).toBeTruthy();
    expect(screen.getByText('20')).toBeTruthy();
});