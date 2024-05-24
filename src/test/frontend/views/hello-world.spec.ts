import { test, expect } from '@playwright/test';


function getUrl() {
  return 'http://localhost:8080/hello-world';
}

test('has hilla public view', async ({ page }) => {
  await page.goto(getUrl());
  await page.getByLabel('Your name').click();
  await page.getByLabel('Your name').fill('Foo');
  await page.getByRole('button', { name: 'Say hello' }).click();
  await expect(page.locator('vaadin-notification-container').first()).toBeVisible();
  await expect(page.locator('vaadin-notification-container')).toContainText('Hello Foo');
});