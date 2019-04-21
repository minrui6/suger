package cn.wolfcode.pmis.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.wolfcode.pmis.domain.Product;
import cn.wolfcode.pmis.service.IProductService;
import cn.wolfcode.pmis.service.impl.ProductServiceImpl;
import cn.wolfcode.pmis.util.StringUtil;
@WebServlet("/product")
public class ProductServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	IProductService service = new ProductServiceImpl();
	@Override
	//根据cmd的值来判断调用什么方法
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String cmd = req.getParameter("cmd");
		if ("saveOrUpdate".equals(cmd)) {
			saveOrUpdate(req, resp);
		}else if ("edit".equals(cmd)) {
			edit(req, resp);
		}else if ("delete".equals(cmd)) {
			delete(req, resp);
		}
		else {
			list(req, resp);
		}
	}
 
	//列表查询
	public void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//调用业务方法处理请求
		List<Product> list = service.listAll();
		//将list存入作用域中
		req.setAttribute("list", list);
		//控制跳转
		req.getRequestDispatcher("/WEB-INF/view/Prouduct/list.jsp").forward(req, resp);
	}
	//删除操作
	public void delete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		//接收请求参数
		String strid = req.getParameter("id");
		if (StringUtil.hasLength(strid)) {
			service.delete(Long.valueOf(strid));
		}
		resp.sendRedirect("/product");
	}
	//编辑操作
	public void edit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//获取id的值
		String strid = req.getParameter("id");
		if (StringUtil.hasLength(strid)) {
			//根据id来查询数据,作为input表单数据回显
			Product product = service.get(Long.valueOf(strid));
			//设置共享数据
			req.setAttribute("p", product);
		}
		req.getRequestDispatcher("/WEB-INF/view/Prouduct/input.jsp").forward(req, resp);
	}

	public void saveOrUpdate(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String strid = req.getParameter("id");
		Product product =new Product();
		req2product(req, product);
		if (StringUtil.hasLength(strid)) {
			product.setId(Long.valueOf(strid));
			service.update(product);
		}else {
			service.save(product);
		}
		resp.sendRedirect("/product");
		
	}
	//把获取的数据封装得到p中
	public void req2product(HttpServletRequest req, Product p){
		String parameter = req.getParameter("productName");
		p.setProductName(parameter);
		String dirid = req.getParameter("dir_id");
		if (StringUtil.hasLength(dirid)) {
			p.setDir_id(Long.valueOf(dirid));
		}
		String sp = req.getParameter("salePrice");
		if (StringUtil.hasLength(sp)) {
			p.setSalePrice(new BigDecimal(sp));
		}
		p.setSupplier(req.getParameter("supplier"));
		p.setBrand(req.getParameter("brand"));
		String ct = req.getParameter("cutoff");
		if (StringUtil.hasLength(ct)) {
			p.setCutoff(Double.valueOf(ct));
		}
		String cp = req.getParameter("costPrice");
		if (StringUtil.hasLength(cp)) {
			p.setCostPrice(new BigDecimal(cp));
		}
	}
}
