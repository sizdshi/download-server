import axios from "axios";

const instance = axios.create({
  baseURL: "http://localhost:8102/api",
  timeout: 1000,
});

export default instance;
