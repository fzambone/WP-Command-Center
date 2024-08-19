import admin, { database } from "firebase-admin";

const serviceAccount = require("./wp-command-center-68f9461439b8.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
});

export const db = admin.firestore();
export const auth = admin.auth();
