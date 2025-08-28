// // Application.js
// import { Alert } from "react-bootstrap";
// import { useEffect, useState } from "react";
// import { Button, Card, Col, Row } from "react-bootstrap";
// import Apis, { endpoints } from "../configs/Apis";
// // import { useSearchParams } from "react-router-dom";
// import MySpinner from "./layout/MySpinner";
// // import cookie from 'react-cookies'

// const Application = () => {
//   const [applications, setApplications] = useState([]);
//   const [loading, setLoading] = useState(false);
//   const [page, setPage] = useState(1);
//   // const [q] = useSearchParams();

//   const loadApplications = async () => {
//     if (page > 0) {
//       try {
//         setLoading(true);
//         let url = `${endpoints['get_all_applications']}?page=${page}`;

//         // let appId = q.get("applicationId");
//         // if (appId) {
//         //   url = `${url}&${appId}`
//         // }

//         let res = await Apis.get(url);

//         if (res.data.length === 0)
//           setPage(0);
//         else {
//           if (page === 1)
//             setApplications(res.data); // reset lần đầu

//           else
//             setApplications([...applications, ...res.data]);

//           //setApplications(res.data);
//         }

//       } catch (ex) {
//         console.error(ex);
//       }
//       finally {
//         setLoading(false);
//       }
//     }
//   };

//   const acceptApplication = async (id) => {
//     try {
//       await Apis.patch(`${endpoints.accept}${id}`);
//       alert("Đã chấp nhận đơn ứng tuyển!");
//       setApplications(applications.filter(app => app.id !== id));
//     } catch (err) {
//       console.error(err);
//       alert("Lỗi khi chấp nhận đơn!");
//     }
//   };

//   const rejectApplication = async (id) => {
//     try {
//       await Apis.patch(`${endpoints.reject}${id}`);
//       alert("Đã từ chối đơn ứng tuyển!");
//       setApplications(applications.filter(app => app.id !== id));
//     } catch (err) {
//       console.error(err);
//       alert("Lỗi khi từ chối đơn!");
//     }
//   };


//   useEffect(() => {
//     loadApplications();

//   },
//     // []);
//     [page]);

//   const loadMore = () => {
//     if (!loading && page > 0)
//       setPage(page + 1);
//   }
//   return (
//     <>
//       {applications.length === 0 && <Alert variant="info" className="mt-1">Không có đơn ứng tuyển nàonào!</Alert>}

//       <Row>
//         {applications.map(p => <Col className="p-1" key={p.id} md={10} xs={4}>
//           <Card style={{ width: '80rem' }}>
//             <Card.Body>
//               <Card.Title>Ứng viên: {p.userId}</Card.Title>
//               <Card.Text>
//                 Thư giới thiệu: {p.coverLetter}
//               </Card.Text>
//               <Card.Img variant="top" src={p.resumeLink} width="300" height="200" />              {/* 🔥 Nếu là Candidate, chỉ hiển thị status */}
//               {/* {role === "CANDIDATE" ? ( */}
//               <Card.Text className="text-info">Trạng thái: {p.status}</Card.Text>
//               {/* ) : ( */}
//               {/* // Nếu là Employer, hiển thị nút Chấp nhận & Từ chối */}
//               <>
//                 <Button className="m-1" variant="primary" onClick={() => acceptApplication(p.id)}>Chấp nhận</Button>
//                 <Button className="m-1" variant="danger" onClick={() => rejectApplication(p.id)}>Từ chối</Button>
//               </>
//               {/* )}  */}
//             </Card.Body>
//           </Card>
//         </Col>)}

//       </Row>
//       {loading && <MySpinner />}
//       {page > 0 && <div className="text-center m-1">
//         <Button variant="success" onClick={loadMore}>Xem thêm...</Button>
//       </div>}
//     </>

//   );
// };

// export default Application;

import { Alert, Button, Card, Col, Row } from "react-bootstrap";
import { useContext, useEffect, useState } from "react";
import Apis, { authApis, endpoints } from "../configs/Apis";
import MySpinner from "./layout/MySpinner";
import { MyDispatchContext, MyUserContext } from "../configs/Contexts";

const Application = () => {
  const [applications, setApplications] = useState([]);
  const [loading, setLoading] = useState(false);
  // const [page, setPage] = useState(1);
  const [cvVisible, setCvVisible] = useState({}); // 🆕 Theo dõi trạng thái hiển thị ảnh CV
  const user = useContext(MyUserContext);

  const loadApplications = async () => {
    console.log(authApis);

    // if (page > 0) {
    try {
      setLoading(true);
      let url = `${endpoints['jaOfUser']}`;
      let res = await authApis().get(url); // ✅ Đúng

      setApplications(res.data);

      // if (res.data.length === 0)
      //   setPage(0);
      // else {
      //   if (page === 1)
      //     setApplications(res.data);
      //   else
      //     setApplications([...applications, ...res.data]);
      // }
    } catch (ex) {
      console.error(ex);
    } finally {
      setLoading(false);
    }
    // }
  };

  const toggleCV = (id) => {
    setCvVisible(prev => ({ ...prev, [id]: !prev[id] }));
  };

  const acceptApplication = async (id) => {
    try {
      await Apis.patch(`${endpoints.accept}${id}`);
      alert("Đã chấp nhận đơn ứng tuyển!");
      setApplications(applications.filter(app => app.id !== id));
    } catch (err) {
      console.error(err);
      alert("Lỗi khi chấp nhận đơn!");
    }
  };

  const rejectApplication = async (id) => {
    try {
      await Apis.patch(`${endpoints.reject}${id}`);
      alert("Đã từ chối đơn ứng tuyển!");
      setApplications(applications.filter(app => app.id !== id));
    } catch (err) {
      console.error(err);
      alert("Lỗi khi từ chối đơn!");
    }
  };

  useEffect(() => {
    loadApplications();
  }, []);

  // const loadMore = () => {
  //   if (!loading && page > 0)
  //     setPage(page + 1);
  // };

  return (
    <>
      {applications.length === 0 && <Alert variant="info" className="mt-1">Không có đơn ứng tuyển nào!</Alert>}

      <Row>
        {applications.map(p => (
          <Col className="p-1" key={p.id} md={10} xs={4}>
            <Card style={{ width: '80rem' }}>
              <Card.Body>
                <Card.Title>Ứng viên: {p.userId}</Card.Title>
                <Card.Text>Thư giới thiệu: {p.coverLetter}</Card.Text>

                <Button
                  variant="info"
                  className="mb-2"
                  onClick={() => toggleCV(p.id)}
                >
                  {cvVisible[p.id] ? "Ẩn CV" : "Xem CV"}
                </Button>

                {cvVisible[p.id] && (
                  <Card.Img
                    variant="top"
                    src={p.resumeLink}
                    alt="CV Ứng viên"
                    style={{ maxWidth: "500px", marginTop: "10px" }}
                  />
                )}

                {/* Trạng thái */}
                <Card.Text
                  className={p.status === 'REJECTED' ? 'text-danger' : 'text-info'}>
                  Trạng thái: {p.status}
                </Card.Text>


                {/* Nút chỉ hiển thị nếu chưa rejected và không phải ứng viên 
                Cần sửa lại user !=='CANDIDATE' */}

                {p.status !== 'REJECTED' && user.role !== 'CANDIDATE' && (
                  <>
                    <Button className="m-1" variant="primary" onClick={() => acceptApplication(p.id)}>
                      Chấp nhận
                    </Button>
                    <Button className="m-1" variant="danger" onClick={() => rejectApplication(p.id)}>
                      Từ chối
                    </Button>
                  </>
                )}
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>

      {loading && <MySpinner />}

      {/* {page > 0 && (
        <div className="text-center m-1">
          <Button variant="success" onClick={loadMore}>Xem thêm...</Button>
        </div>
      )} */}
    </>
  );
};

export default Application;
