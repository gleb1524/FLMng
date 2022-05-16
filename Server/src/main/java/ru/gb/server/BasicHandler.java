package ru.gb.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.gb.dto.AuthRequest;
import ru.gb.dto.BasicResponse;
import ru.gb.dto.RegRequest;
import ru.gb.dto.UploadRequest;

import java.io.FileOutputStream;

public class BasicHandler extends ChannelInboundHandlerAdapter {

    DataBaseService dataBaseService = new DataBaseService();
    private  String login;
    private  String password;
    private  String auth;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof UploadRequest){
            UploadRequest request = (UploadRequest) msg;
            String pathOfFile = String.format(((UploadRequest) request).getRemPath()+"\\%s",((UploadRequest)request).getFilename());
            System.out.println(pathOfFile);
            try (FileOutputStream fos = new FileOutputStream(pathOfFile)) {
                fos.write(( request).getData());
            }
        }
        if(msg instanceof RegRequest){
            RegRequest request = (RegRequest) msg;
            if(!dataBaseService.hasRegistration(request.getLogin())){
                dataBaseService.creatRegistration(request.getLogin(), request.getPassword(),
                        request.getName(), request.getSurname());
                ctx.writeAndFlush(new BasicResponse("reg_ok"));
            }else{
                ctx.writeAndFlush(new BasicResponse("reg_no"));
            }
        }else if(msg instanceof AuthRequest) {
            AuthRequest request = (AuthRequest) msg;
            login = request.getLogin();
            password = request.getPassword();
            if(dataBaseService.hasAuth(login, password)){
                ctx.writeAndFlush(new BasicResponse("auth_ok"));
                auth = dataBaseService.creatAuth(request.getLogin(), request.getPassword());
                ctx.writeAndFlush(new BasicResponse("auth "+ auth));
                System.out.println(auth);
            }else{
                ctx.writeAndFlush(new BasicResponse("auth_no"));
            }
        }
        else{
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
