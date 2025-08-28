// import { useContext, useEffect, useState } from "react";
// import { Button, Container, Form, Nav, Navbar, NavDropdown } from "react-bootstrap";
// import { Link, useNavigate } from "react-router-dom";
// import Apis, { endpoints } from "../../configs/Apis";
// import { MyDispatchContext, MyUserContext } from "../../configs/Contexts";
// import axios from 'axios';

// const Header = () => {
//     const [categories, setCategories] = useState([]);
//     const [kw, setKw] = useState();

//     const nav = useNavigate();
//     const user = useContext(MyUserContext);
//     const dispatch = useContext(MyDispatchContext);
//     useEffect(() => {
//         // Lấy danh sách danh mục
//         axios.get(endpoints.categories)
//             .then((response) => {
//                 setCategories(response.data);
//             })
//             .catch((error) => console.error('Lỗi khi tìm danh mục:', error));
//     }, []);

//     const handleLoginRegister = () => {
//         nav('/login'); // Hoặc mở modal đăng nhập/đăng ký nếu cần
//     };


//     return (

//         <Navbar expand="lg" className="bg-body-tertiary">
//             <Container>
//                 <Navbar.Brand as={Link} to="/" className="fw-bold text-dark">
//                     <h1 className="display-6 d-inline">JobSearch</h1>
//                     <span className="ms-2 text-muted">Tìm việc dễ dàng</span>
//                 </Navbar.Brand>

//                 {/* Liên kết giữa */}
//                 <Nav className="mx-auto">
//                     <Link to="/" className="nav-link">Trang chủ</Link>
//                 </Nav>

//                 {user === null ? <>
//                     <Link to="/register" className="nav-link text-success">Đăng ký</Link>
//                     <Link to="/login" className="nav-link text-danger">Đăng nhập</Link>

//                     <Link to="/jaOfUser" className="nav-link text-danger">Danh sách ứng tuyển</Link>
//                     <Link to="/companies" className="nav-link text-danger">Các công ty</Link>
//                     <Link to="/users-work-with" className="nav-link text-danger">Đánh giá</Link>
//                     <Link to="/stats" className="nav-link text-danger">Thống kê báo cáo</Link>
//                     <Link to="/inactiveUser" className="nav-link text-danger">Duyệt nhà tuyển dụng</Link>

//                 </> : <>
//                     <Link to="/" className="nav-link text-success">Chào {user.username}!</Link>
//                     {user.role === 'CANDIDATE' ? <>
//                         <Link to="/jaOfUser" className="nav-link text-danger">Danh sách ứng tuyển</Link>
//                         <Link to="/companies" className="nav-link text-danger">Các công ty</Link>
//                         <Link to="/users-work-with" className="nav-link text-danger">Đánh giá</Link>
//                     </> : <>
//                         {user.role === 'EMPLOYER' ? <>
//                             <Link to="/jaOfUser" className="nav-link text-danger">Danh sách ứng tuyển</Link>
//                             <Link to="/users-work-with" className="nav-link text-danger">Đánh giá</Link>

//                         </> : <>
//                             <Link to="/get_all_applications" className="nav-link text-danger">Danh sách ứng tuyển</Link>
//                             <Link to="/inactiveUser" className="nav-link text-danger">Duyệt nhà tuyển dụng</Link>
//                             <Link to="/stats" className="nav-link text-danger">Thống kê báo cáo</Link>


//                         </>}
//                     </>}

//                     <Button className="btn btnd-danger" onClick={() => dispatch({ "type": "logout" })}>Đăng xuất</Button>
//                 </>}




//                 {/* <Form onSubmit={search} className="d-flex">
//                             <Form.Control value={kw} onChange={e => setKw(e.target.value)}
//                                 type="search"
//                                 placeholder="Tìm kiếm..."
//                                 className="me-2"
//                                 aria-label="Search"
//                             />
//                             <Button type="submit" variant="outline-success">Tìm</Button>
//                         </Form> */}

//             </Container >
//         </Navbar >

//     );
// }

// export default Header;
import { useContext, useEffect, useState } from "react";
import { Button, Container, Nav, Navbar } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import axios from 'axios';
import Apis, { endpoints } from "../../configs/Apis";
import { MyDispatchContext, MyUserContext } from "../../configs/Contexts";

const Header = () => {
    const [categories, setCategories] = useState([]);
    const nav = useNavigate();
    const user = useContext(MyUserContext);
    const dispatch = useContext(MyDispatchContext);

    useEffect(() => {
        axios.get(endpoints.categories)
            .then((res) => setCategories(res.data))
            .catch((err) => console.error("Lỗi khi tải danh mục:", err));
    }, []);

    return (
        <Navbar expand="lg" className="bg-body-tertiary px-0">
            <Container fluid className="px-3 d-flex align-items-center">
                {/* Logo và brand */}
                <Navbar.Brand as={Link} to="/" className="fw-bold text-dark me-4">
                    <h1 className="display-6 d-inline">JobSearch</h1>
                    <span className="ms-2 text-muted">Tìm việc dễ dàng</span>
                </Navbar.Brand>

                {/* Các link điều hướng nằm bên trái */}
                <Nav className="d-flex align-items-center flex-wrap">
                    <Link to="/" className="nav-link mx-2">Trang chủ</Link>

                    {user === null ? (
                        <>
                            <Link to="/register" className="nav-link text-success mx-2">Đăng ký</Link>
                            <Link to="/login" className="nav-link text-danger mx-2">Đăng nhập</Link>
                            <Link to="/companies" className="nav-link text-danger mx-2">Các công ty</Link>
                            <Link to="/users-work-with" className="nav-link text-danger mx-2">Đánh giá</Link>
                            <Link to="/stats" className="nav-link text-danger mx-2">Thống kê báo cáo</Link>
                            <Link to="/inactiveUser" className="nav-link text-danger mx-2">Duyệt nhà tuyển dụng</Link>
                        </>
                    ) : (
                        <>
                            {user.role === 'CANDIDATE' && (
                                <>

                                    <Link to="/jaOfUser" className="nav-link text-danger mx-2">Danh sách ứng tuyển</Link>
                                    <Link to="/companies" className="nav-link text-danger mx-2">Các công ty</Link>
                                    <Link to="/users-work-with" className="nav-link text-danger mx-2">Đánh giá</Link>
                                </>
                            )}

                            {user.role === 'EMPLOYER' && (
                                <>
                                    <Link to="/jaOfUser" className="nav-link text-danger mx-2">Danh sách ứng tuyển</Link>
                                    <Link to="/users-work-with" className="nav-link text-danger mx-2">Đánh giá</Link>
                                </>
                            )}

                            {user.role === 'ADMIN' && (
                                <>
                                    <Link to="/companies" className="nav-link text-danger mx-2">Các công ty</Link>
                                    <Link to="/get_all_applications" className="nav-link text-danger mx-2">Danh sách ứng tuyển</Link>
                                    <Link to="/inactiveUser" className="nav-link text-danger mx-2">Duyệt nhà tuyển dụng</Link>
                                    <Link to="/stats" className="nav-link text-danger mx-2">Thống kê báo cáo</Link>
                                </>
                            )}
                        </>
                    )}
                </Nav>

                {/* Phần chào user và nút đăng xuất sát bên phải */}
                {user !== null && (
                    <div className="ms-auto d-flex align-items-center">
                        <span className="me-3 fw-semibold text-success">Chào {user.username}!</span>
                        <Button className="btn btn-danger" onClick={() => dispatch({ type: "logout" })}>
                            Đăng xuất
                        </Button>
                    </div>
                )}
            </Container>
        </Navbar>
    );
};

export default Header;
