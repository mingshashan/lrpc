package com.mss.gtr.lrpc.facade;

import com.mss.gtr.lrpc.facade.dto.response.BookDTO;
import com.mss.gtr.lrpc.facade.dto.response.Response;

public interface BookFacade {

    Response<BookDTO> getBookByCode(String bookCode);

}
