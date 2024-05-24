import { test, expect } from '@playwright/test';


function getUrl() {
  return 'http://localhost:8080/hello-world4';
}

async function login(page, username, password) {
  await page.getByLabel('Username').click();
  await page.getByLabel('Username').fill(username);
  await page.getByLabel('Username').press('Tab');
  await page.getByLabel('Password', { exact: true }).fill(password);
  await page.getByRole('button', { name: 'Log in' }).click();
}

test('has hilla admin view', async ({ page }) => {
  await page.goto(getUrl());
  await login(page, 'admin', 'admin');
  await page.getByLabel('Your name').click();
  await page.getByLabel('Your name').fill('Foo');
  await page.getByRole('button', { name: 'Say hello' }).click();
  await expect(page.locator('vaadin-notification-container').first()).toBeVisible();
  await expect(page.locator('vaadin-notification-container')).toContainText('Hello Foo');
});