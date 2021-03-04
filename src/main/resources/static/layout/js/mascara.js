function mask(o, f) {
    setTimeout(function () {
        var v = f(o.value);
        if (v !== o.value) {
            o.value = v;
        }
    }, 1);
}

function mphone(v) {
    var r = v.replace(/\D/g, "");
    r = r.replace(/^0/, "");
    if (r.length > 10) {
        // 11+ digits. Format as 5+4.
        r = r.replace(/^(\d\d)(\d{5})(\d{4}).*/, "($1) $2-$3");
    } else if (r.length > 5) {
        // 6..10 digits. Format as 4+4
        r = r.replace(/^(\d\d)(\d{4})(\d{0,4}).*/, "($1) $2-$3");
    } else if (r.length > 2) {
        // 3..5 digits. Add (0XX..)
        r = r.replace(/^(\d\d)(\d{0,5})/, "($1) $2");
    } else {
        // 0..2 digits. Just add (0XX
        r = r.replace(/^(\d*)/, "($1");
    }
    return r;
}


function fMasc(objeto, mascara) {
    obj = objeto;
    masc = mascara;
    setTimeout("fMascEx()", 1);
}
function fMascEx() {
    obj.value = masc(obj.value);
}
function mTel(tel) {
    tel = tel.replace(/\D/g, "");
    tel = tel.replace(/^(\d)/, "($1");
    tel = tel.replace(/(.{3})(\d)/, "$1)$2");
    if (tel.length === 9) {
        tel = tel.replace(/(.{1})$/, "-$1");
    } else if (tel.length === 10) {
        tel = tel.replace(/(.{2})$/, "-$1");
    } else if (tel.length === 11) {
        tel = tel.replace(/(.{3})$/, "-$1");
    } else if (tel.length === 12) {
        tel = tel.replace(/(.{4})$/, "-$1");
    } else if (tel.length > 12) {
        tel = tel.replace(/(.{4})$/, "-$1");
    }
    return tel;
}
function mCNPJ(cnpj) {
    cnpj = cnpj.replace(/\D/g, "");
    cnpj = cnpj.replace(/^(\d{2})(\d)/, "$1.$2");
    cnpj = cnpj.replace(/^(\d{2})\.(\d{3})(\d)/, "$1.$2.$3");
    cnpj = cnpj.replace(/\.(\d{3})(\d)/, ".$1/$2");
    cnpj = cnpj.replace(/(\d{4})(\d)/, "$1-$2");
    return cnpj;
}
function mCPF(cpf) {
    cpf = cpf.replace(/\D/g, "");
    cpf = cpf.replace(/(\d{3})(\d)/, "$1.$2");
    cpf = cpf.replace(/(\d{3})(\d)/, "$1.$2");
    cpf = cpf.replace(/(\d{3})(\d{1,2})$/, "$1-$2");
    return cpf;
}
function mCEP(cep) {
    cep = cep.replace(/\D/g, "");
    cep = cep.replace(/^(\d{2})(\d)/, "$1.$2");
    cep = cep.replace(/\.(\d{3})(\d)/, ".$1-$2");
    return cep;
}
function mNum(num) {
    num = num.replace(/\D/g, "");
    return num;
}

function moeda(a, e, r, t) {

    if (a.value.replace(/[^\d]/g, '').length > a.getAttribute("maxlength") - 1)
        return;

    let n = ""
            , h = j = 0
            , u = tamanho2 = 0
            , l = ajd2 = ""
            , o = window.Event ? t.which : t.keyCode;
    if (13 == o || 8 == o)
        return !0;
    if (n = String.fromCharCode(o),
            -1 == "0123456789".indexOf(n))
        return !1;
    for (u = a.value.length,
            h = 0; h < u && ("0" == a.value.charAt(h) || a.value.charAt(h) == r); h++)
        ;
    for (l = ""; h < u; h++)
        -1 != "0123456789".indexOf(a.value.charAt(h)) && (l += a.value.charAt(h));
    if (l += n,
            0 == (u = l.length) && (a.value = ""),
            1 == u && (a.value = "0" + r + "0" + l),
            2 == u && (a.value = "0" + r + l),
            u > 2) {
        for (ajd2 = "",
                j = 0,
                h = u - 3; h >= 0; h--)
            3 == j && (ajd2 += e,
                    j = 0),
                    ajd2 += l.charAt(h),
                    j++;
        for (a.value = "",
                tamanho2 = ajd2.length,
                h = tamanho2 - 1; h >= 0; h--)
            a.value += ajd2.charAt(h);
        a.value += r + l.substr(u - 2, u)

    }
    return !1;
}

function calcular() {
    var valor = document.getElementById("valor");
    var desconto = document.getElementById("desconto");
    var valorTotal = document.getElementById("valorTotal");

    valorTotal.value = parseFloat(valor.value) - (parseFloat(valor.value) * parseFloat(desconto.value) / 100);
    console.log(parseFloat(valorTotal.value));
    return valorTotal.innerHTML;
}

function formatarMoeda() {
    var elemento = document.getElementById('valor');
    var valor = elemento.value;

    valor = valor + '';
    valor = parseInt(valor.replace(/[\D]+/g, ''));
    valor = valor + '';
    valor = valor.replace(/([0-9]{2})$/g, ",$1");

    if (valor.length > 6) {
        valor = valor.replace(/([0-9]{3}),([0-9]{2}$)/g, ".$1,$2");
    }

    elemento.value = valor;
}
