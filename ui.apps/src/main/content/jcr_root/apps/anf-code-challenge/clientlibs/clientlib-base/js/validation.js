$(document).on("click", "#testform form button", function() {
    const country = $('.country__cmp span')[0].dataset.country;
    if (!$('[name="firstname"]').val() || !$('[name="lastname"]').val() || !$('[name="age"]').val() || !country) {
        alert('Please fill in input fields');
    } else {
        $.getJSON('/etc/age.json', {}, function(data) {
            let minAge = data.minAge;
            let maxAge = data.maxAge;
            var reqParams = {
                'firstname': $('[name="firstname"]').val(),
                'lastname': $('[name="lastname"]').val(),
                'age': $('[name="age"]').val(),
                'country': country
            }
            if (Number(minAge) <= Number(reqParams.age) && Number(maxAge) >= Number(reqParams.age)) {

                $.get('/bin/saveUserDetails', reqParams, function(data) {
                    if (data === "OK") {
                        alert('User data inserted');
                    } else {
                        alert('Something went wrong!');
                    }
                });
            } else {
                alert("Age should be between" + Number(data.minAge) + "&" + Number(data.maxAge));
            }
        });
    }
    return false;
});