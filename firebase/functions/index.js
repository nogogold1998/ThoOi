const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();

const FULL_NAME = 'full_name'
const TEXT = 'text'
const RECEIVER = 'receiver'
const SENDER = 'sender'
const TOKEN = 'token'
const CUSTOMER = 'customer'
const FIXER_ID = 'fixer_id'
const FIXERS_ID = 'fixers_id'
const NOTIFICATION = 'notification'
const TYPE_MSG = 'message'
const MSG_ACCEPT = 'Đã chấp nhận yêu cầu của bạn, liên hệ ngay nào'
const MSG_REJECT = 'Yêu cầu của bạn đã bị bỏ qua'
const MSG_FIXER = 'Đã yêu cầu nhận công việc'

const db = admin.firestore()

exports.onCreateMessage = functions.firestore
    .document("/conversations/{conversationId}/messages/{messageId}")
    .onCreate(snapshot => {
        const text = snapshot.get(TEXT)
        return sendNotification(TYPE_MSG, snapshot.get(SENDER), snapshot.get(RECEIVER), text)
    })

exports.onUpdatePost = functions.firestore
    .document("/posts/{postId}")
    .onUpdate(change => {
        const fixerId = change.after.get(FIXER_ID)
        const fixersId = change.after.get(FIXERS_ID)
        const customer = change.after.get(CUSTOMER)
        let notifications = []
        if (fixersId && fixersId.length) {
            const users = db.collection('users')
            const customerRef = users.doc(`${customer}`)
            if (fixerId) {
                fixersId.forEach(id => {
                    const fixerRef = users.doc(`${id}`)
                    notifications.push(sendNotification(NOTIFICATION, customerRef, fixerRef, fixerId === id ? MSG_ACCEPT : MSG_REJECT))
                })
            } else {
                const lastFixerRef = users.doc(`${fixersId[fixersId.length - 1]}`)
                notifications.push(sendNotification(NOTIFICATION, lastFixerRef, customerRef, MSG_FIXER))
            }
        }
        return Promise.all(notifications)
    })

function sendNotification(type, senderRef, receiverRef, text) {
    let name = CUSTOMER;
    return senderRef.get()
        .then(sender => {
            name = sender.get(FULL_NAME)
            return receiverRef.get()
        })
        .then(receiver => {
            const tokens = receiver.get(TOKEN)
            let messages = []
            if (tokens && tokens.length) {
                tokens.forEach(token => {
                    const message = {notification: {title: name, body: text}}
                    messages.push(admin.messaging().sendToDevice(token, message))
                })
            }
            return Promise.all(messages)
        })
        .then(() => {
            const notification = {
                created_at: Date.now(),
                content: text,
                is_read: false,
                sender: senderRef,
                receiver: receiverRef,
                type: type,
            }
            return db.collection('notifications').add(notification)
        })
        .catch(reason => console.log(reason))
}
