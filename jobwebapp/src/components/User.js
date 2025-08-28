// import { useEffect, useState } from "react";
// import { Alert, Card, Col, Row, Spinner } from "react-bootstrap";
// import Apis, { endpoints } from "../configs/Apis";
// import MySpinner from "./layout/MySpinner";

// const WorkedWithUsers = () => {
//     const [users, setUsers] = useState([]);
//     const [loading, setLoading] = useState(false);

//     const loadUsers = async () => {
//         try {
//             setLoading(true);
//             //const res = await Apis.get(endpoints.workedWith);

//             const res = await Apis.get(endpoints.fbofUser);

//             setUsers(res.data);
//         } catch (err) {
//             console.error("Lỗi khi tải danh sách người dùng đã làm việc cùng", err);
//         } finally {
//             setLoading(false);
//         }
//     };

//     useEffect(() => {
//         loadUsers();
//     }, []);

//     return (
//         <>
//             <h2>Người dùng đã làm việc cùng bạn</h2>

//             {loading && <MySpinner />}

//             {users.length === 0 && !loading ? (
//                 <Alert variant="info">Không có người dùng nào!</Alert>
//             ) : (
//                 <Row>
//                     {users.map((u) => (
//                         <Col key={u.id} md={6} className="mb-3">
//                             <Card>
//                                 <Card.Body>
//                                     <Card.Title>{u.lastName} {u.firstName}</Card.Title>
//                                     <Card.Text>Email: {u.email}</Card.Text>
//                                     <Card.Text>Username: {u.username}</Card.Text>
//                                 </Card.Body>
//                             </Card>
//                         </Col>
//                     ))}
//                 </Row>
//             )}
//         </>
//     );
// };

// export default WorkedWithUsers;

import { useEffect, useState } from "react";
import { Alert, Card, Col, Row, Button, Modal, Form } from "react-bootstrap";
import Apis, { endpoints, authApis } from "../configs/Apis";
import MySpinner from "./layout/MySpinner";

const WorkedWithUsers = () => {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(false);

    const [showFeedbackModal, setShowFeedbackModal] = useState(false);
    const [showViewFeedbackModal, setShowViewFeedbackModal] = useState(false);

    const [selectedUser, setSelectedUser] = useState(null);
    const [rating, setRating] = useState(5);
    const [comment, setComment] = useState("");
    const [feedbackList, setFeedbackList] = useState([]);

    // Load danh sách người dùng đã làm việc cùng
    // const loadUsers = async () => {
    //     try {
    //         setLoading(true);
    //         const res = await authApis().get(endpoints.users_work_with);
    //         setUsers(res.data);
    //     } catch (err) {
    //         console.error("Lỗi khi tải danh sách người dùng đã làm việc cùng", err);
    //     } finally {
    //         setLoading(false);
    //     }
    // };
    const loadUsers = async () => {
        try {
            setLoading(true);
            const res = await authApis().get(endpoints.users_work_with);
            const usersData = res.data;

            const enrichedUsers = await Promise.all(usersData.map(async (u) => {
                try {
                    const companyRes = await authApis().get(`${endpoints.companyOfUser}${u.id}`);
                    return {
                        ...u,
                        company: companyRes.data // nếu không có company thì sẽ là null
                    };
                } catch (e) {
                    return { ...u, company: null };
                }
            }));

            setUsers(enrichedUsers);
        } catch (err) {
            console.error("Lỗi khi tải danh sách người dùng đã làm việc cùng", err);
        } finally {
            setLoading(false);
        }
    };


    useEffect(() => {
        loadUsers();
    }, []);

    const openFeedbackModal = (user) => {
        setSelectedUser(user);
        setRating(5);
        setComment("");
        setShowFeedbackModal(true);
    };

    const submitFeedback = async () => {
        try {
            await authApis().post(`${endpoints.addFeedback}${selectedUser.id}`, null, {
                params: {
                    rating: rating,
                    comment: comment,
                },
                //   withCredentials: true, // nếu dùng session hoặc JWT cookie
            });

            alert("Đánh giá thành công!");
            setShowFeedbackModal(false);
        } catch (err) {
            console.error("Lỗi khi gửi đánh giá", err);
            alert("Không thể gửi đánh giá!");
        }
    };

    const openViewFeedbackModal = async (user) => {
        try {
            setSelectedUser(user);
            const res = await authApis().get(`${endpoints.fbOfUser}${user.id}`);
            setFeedbackList(res.data);
            setShowViewFeedbackModal(true);
        } catch (err) {
            console.error("Lỗi khi tải đánh giá", err);
            alert("Không thể tải đánh giá!");
        }
    };

    return (
        <>
            <h2>Người dùng đã làm việc cùng bạn</h2>

            {loading && <MySpinner />}

            {users.length === 0 && !loading ? (
                <Alert variant="info">Không có người dùng nào!</Alert>
            ) : (
                <Row>
                    {users.map((u) => (
                        <Col key={u.id} md={6} className="mb-3">
                            <Card>
                                {/* <Card.Body>
                                    <Card.Title>{u.lastName} {u.firstName}</Card.Title>
                                    <Card.Text>Email: {u.email}</Card.Text>
                                    <Card.Text>Username: {u.username}</Card.Text>
                                    
                                    <Button
                                        variant="success"
                                        className="me-2"
                                        onClick={() => openFeedbackModal(u)}
                                    >
                                        Đánh giá
                                    </Button>

                                    <Button
                                        variant="info"
                                        onClick={() => openViewFeedbackModal(u)}
                                    >
                                        Xem đánh giá
                                    </Button>
                                </Card.Body> */}
                                <Card.Body>
                                    <Card.Title>Nhà tuyển dụng: {u.lastName} {u.firstName}</Card.Title>
                                    <Card.Text>Username: {u.username}</Card.Text>


                                    {u.company ? (
                                        <>

                                            <Card.Title>Công ty: {u.company.name}</Card.Title>
                                            <Card.Text>{u.company.description}</Card.Text>
                                            <Row>
                                                {u.company.image1 && (
                                                    <Col xs={4} className="mb-2">
                                                        <img src={u.company.image1} alt="image1" className="img-fluid rounded" />
                                                    </Col>
                                                )}
                                                {u.company.image2 && (
                                                    <Col xs={4} className="mb-2">
                                                        <img src={u.company.image2} alt="image2" className="img-fluid rounded" />
                                                    </Col>
                                                )}
                                                {u.company.image3 && (
                                                    <Col xs={4} className="mb-2">
                                                        <img src={u.company.image3} alt="image3" className="img-fluid rounded" />
                                                    </Col>
                                                )}
                                            </Row>
                                        </>
                                    ) : (
                                        <>
                                            <Card.Text>Email: {u.email}</Card.Text>
                                        </>
                                    )}
                                    <Button variant="success" className="me-2" onClick={() => openFeedbackModal(u)}>
                                        Đánh giá
                                    </Button>
                                    <Button variant="info" onClick={() => openViewFeedbackModal(u)}>
                                        Xem đánh giá
                                    </Button>
                                </Card.Body>

                            </Card>
                        </Col>
                    ))}
                </Row>
            )}

            {/* Modal đánh giá */}
            <Modal show={showFeedbackModal} onHide={() => setShowFeedbackModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Đánh giá người dùng</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form.Group className="mb-3">
                        <Form.Label>Điểm đánh giá (1 - 5):</Form.Label>
                        <Form.Control
                            type="number"
                            min={1}
                            max={5}
                            value={rating}
                            onChange={(e) => setRating(e.target.value)}
                        />
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Form.Label>Bình luận:</Form.Label>
                        <Form.Control
                            as="textarea"
                            rows={3}
                            value={comment}
                            onChange={(e) => setComment(e.target.value)}
                        />
                    </Form.Group>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowFeedbackModal(false)}>
                        Hủy
                    </Button>
                    <Button variant="primary" onClick={submitFeedback}>
                        Gửi đánh giá
                    </Button>
                </Modal.Footer>
            </Modal>

            {/* Modal xem đánh giá */}
            <Modal show={showViewFeedbackModal} onHide={() => setShowViewFeedbackModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Các đánh giá về {selectedUser?.lastName} {selectedUser?.firstName}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {feedbackList.length === 0 ? (
                        <p>Chưa có đánh giá nào.</p>
                    ) : (
                        feedbackList.map((fb, idx) => (
                            <div key={idx} className="mb-3 border-bottom pb-2">
                                <strong>Từ: </strong>{fb.fromUserId.username}<br />
                                <strong>Điểm:</strong> {fb.rating} ⭐<br />
                                <strong>Bình luận:</strong> {fb.comment}<br />
                            </div>
                        ))
                    )}
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowViewFeedbackModal(false)}>
                        Đóng
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
};

export default WorkedWithUsers;

