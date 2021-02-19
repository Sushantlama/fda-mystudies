// Replace the example domain name with your deployed address.
export const environment = {
  production: true,
  // remove http/https to appear relative. xsrf-token skips absolute paths.
  participantManagerDatastoreUrl:
    'http://35.222.67.4:8081/participant-manager-datastore',
  baseHref: '/participant-manager/',
  hydraLoginUrl: 'https://35.222.67.4:9000/oauth2/auth',
  authServerUrl: 'http://35.222.67.4:8084/auth-server',
  authServerRedirectUrl: 'https://34.69.210.52/dev/auth-server/callback',
  hydraClientId: 'MYSTUDIES_OAUTH_CLIENT',
  appVersion: 'v0.1',
  termsPageTitle: 'Terms title goes here',
  termsPageDescription: 'Terms description goes here',
  aboutPageTitle: 'About page title goes here',
  aboutPageDescription: 'About page description goes here',
  copyright: 'Copyright 2020-2021 Google LLC.',
};
