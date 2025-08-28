import { BrowserRouter, Route, Routes } from "react-router-dom";
import Header from "./components/layout/Header";
import Footer from "./components/layout/Footer";
import Home from "./components/Home";
import 'bootstrap/dist/css/bootstrap.min.css';
import { Container } from "react-bootstrap";
import Application from "./components/Application";
import Company from "./components/Company";
import User from "./components/User";
import Stats from "./components/Stats";
import ActiveUser from "./components/ActiveUser";

import Login from './components/Login';
import JobList from './components/JobList';
import Register from './components/Register';
import AllApplications from './components/AllApplications';
import ApplyJobForm from './components/ApplyJobForm';

import Profile from './components/Profile';


import { MyCartContext, MyDispatchContext, MyUserContext, UserProvider } from './configs/Contexts';
import { useReducer } from "react";
import MyUserReducer from "./reducers/MyUserReducer";

const App = () => {
  const [user, dispatch] = useReducer(MyUserReducer, null);

  return (
    <UserProvider> {/* Sử dụng UserProvider thay vì tự quản lý useReducer */}
      <BrowserRouter>
        <Header />

        <Container>
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login />} />
            <Route path="/users-work-with" element={< User />} />
            <Route path="/users" element={<Register />} />
            <Route path="/get_all_applications" element={<AllApplications />} />
            <Route path="/apply" element={<ApplyJobForm />} />

            <Route path="/jaOfUser" element={<Application />} />
            <Route path="/companies" element={<Company />} />
            <Route path="/feedback" element={<User />} />
            <Route path="/inactiveUser" element={<ActiveUser />} />
            <Route path="/stats" element={<Stats />} />
          </Routes>
        </Container>

        {/* <Footer /> */}
      </BrowserRouter>
    </UserProvider>

  );
}

export default App;