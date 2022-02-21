package chainofresponsibility;

public class Test {
    public static void main(String[] args) {
        RequestHandle hr = new HRRequestHandle();
        RequestHandle pm = new PMRequestHandle(hr);
        RequestHandle tl = new TLRequestHandle(pm);

        Request request = new LeaveRequest();
        tl.handleRequest(request);

        request = new AddMoneyRequest();
        tl.handleRequest(request);

        request = new DimissionRequest();
        tl.handleRequest(request);
    }
}
