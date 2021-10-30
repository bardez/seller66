const Email = require('email-templates');
import nodemailer from 'nodemailer'

const transport = nodemailer.createTransport({
  // service: process.env.SERVICE, // if use gmail sender
  // if sender is gmail, this block is unnecessary
  host: process.env.MAIL_HOST,
  port: process.env.MAIL_PORT,
  secure: true,
  // --------–------------------------------------
  auth: {
    user: process.env.USER_MAIL,
    pass: process.env.USER_PASS
  }
});

export const sendMail = async (mailOptions) =>{

  const email = new Email({
    transport,
    send: true,
    preview: false // set true, to show email preview on dev
  });

  try {
    console.log('Sending mail...', mailOptions);
    await email.send(mailOptions);
  } catch (error) {
    console.log('error to send e-mail -> ', error);
    return error;
  }
}